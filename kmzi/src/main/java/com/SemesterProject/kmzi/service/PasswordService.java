package com.SemesterProject.kmzi.service;


import com.SemesterProject.kmzi.cassandra.PasswordRepo;
import com.SemesterProject.kmzi.entity.Password;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private PasswordRepo passwordRepository;

    @Autowired
    public PasswordService passwordService;

    @Autowired
    @Qualifier("cassandraTemplate")
    private CassandraOperations cassandraTemplate;

    @Autowired
    @Qualifier("db2CassandraTemplate")
    private CassandraOperations db2CassandraTemplate2;


    @PostConstruct
    public void prepareDataBases() {
        createKeyspace();
        createPasswordTable();
    }

    public List<Password> getPasswords() {
        return passwordRepository.findAll();
    }

    public void savePassword(Password password) {
        passwordRepository.save(password);
    }

    public void deletePassword(Password password) {
        passwordRepository.delete(password);
    }

    public String createRulesForPassword() {
        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        LCR.setNumberOfCharacters(5);
        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        UCR.setNumberOfCharacters(2);
        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        DR.setNumberOfCharacters(5);
        CharacterRule SR = new CharacterRule(EnglishCharacterData.Special);
        SR.setNumberOfCharacters(2);
        PasswordGenerator passGen = new PasswordGenerator();
        String password = passGen.generatePassword(24, SR, LCR, UCR, DR);
        return password;
    }

    public double calculateEntropy(String password) {
        int passwordLength = password.length();
        int numberOfLowerCaseCharacters = 0;
        int numberOfUpperCaseCharacters = 0;
        int numberOfNumbers = 0;
        int numberOfSpecialSymbols = 0;

        for (int i = 0; i < passwordLength; i++) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                numberOfLowerCaseCharacters++;
            } else if (Character.isUpperCase(c)) {
                numberOfUpperCaseCharacters++;
            } else if (Character.isDigit(c)) {
                numberOfNumbers++;
            } else {
                numberOfSpecialSymbols++;
            }
        }

        int possibleCharacters = numberOfLowerCaseCharacters + numberOfUpperCaseCharacters
                + numberOfNumbers + numberOfSpecialSymbols;

        double entropy = Math.log(possibleCharacters) / Math.log(2) * passwordLength;

        return entropy;
    }

    public String decryptPassword(String encryptedPassword, String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = hexStringToByteArray(encryptedPassword);
        // separate the IV and the cipher text
        byte[] iv = Arrays.copyOfRange(encryptedBytes, 0, 16);
        byte[] cipherText = Arrays.copyOfRange(encryptedBytes, 16, encryptedBytes.length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] original = cipher.doFinal(cipherText);
            return new String(original);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String getDecryptedPassword(String secret, String credentialId) {
        String wholePassword = getWholePassword(credentialId);
        return decryptPassword(wholePassword, secret);

    }

    public String getWholePassword(String id) {
        String selectCql = "SELECT * FROM user_passwords.password WHERE credential_id = ? ALLOW FILTERING";
        SimpleStatement statement = SimpleStatement.builder(selectCql).addPositionalValue(Integer.valueOf(id)).build();
        ResultSet resultSetDb1 = cassandraTemplate.execute(statement);
        String passwordFirstPart = resultSetDb1.one().getString("hash");
        ResultSet resultSetDb2 = db2CassandraTemplate2.execute(statement);
        String passwordSecondPart = resultSetDb2.one().getString("hash");
        return passwordFirstPart + passwordSecondPart;
    }

    public void splitPassword(String password, Integer id) {
        UUID cassandraId = UUID.randomUUID();
        String firstPart = password.substring(0, password.length() / 2);
        String secondPart = password.substring(password.length() / 2);
        writeToDb1(cassandraId, firstPart, id);
        writeToDb2(cassandraId, secondPart, id);
    }

    public void writeToDb1(UUID cassandraId, String passwordHash, Integer id) {
        String insertCql = "INSERT INTO user_passwords.password (id, hash, credential_id) VALUES (?, ?, ?)";
        SimpleStatement insertPassword = SimpleStatement.builder(insertCql).addPositionalValues(cassandraId, passwordHash, id).build();
        cassandraTemplate.execute(insertPassword);
    }

    public void writeToDb2(UUID cassandraId, String passwordHashSecondPart, Integer id) {
        String insertCql = "INSERT INTO user_passwords.password (id, hash, credential_id) VALUES (?, ?, ?)";
        SimpleStatement insertPassword = SimpleStatement.builder(insertCql).addPositionalValues(cassandraId, passwordHashSecondPart, id).build();
        db2CassandraTemplate2.execute(insertPassword);
    }

    public void createKeyspace() {
        String createKeyspace = "CREATE KEYSPACE IF NOT EXISTS user_passwords\n" +
                "  WITH REPLICATION = { \n" +
                "   'class' : 'SimpleStrategy', \n" +
                "   'replication_factor' : 1 \n" +
                "  };";
        SimpleStatement statement = SimpleStatement.builder(createKeyspace).build();
        cassandraTemplate.execute(statement);
        db2CassandraTemplate2.execute(statement);


    }

    public void createPasswordTable() {
        String createTableCql = "CREATE TABLE IF NOT EXISTS user_passwords.password (" + "id UUID PRIMARY KEY, " + "hash text, " + "credential_id int" + ")";
        SimpleStatement statement = SimpleStatement.builder(createTableCql).build();
        cassandraTemplate.execute(statement);
        db2CassandraTemplate2.execute(statement);
    }


}
