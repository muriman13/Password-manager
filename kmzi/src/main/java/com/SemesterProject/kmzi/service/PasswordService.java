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
import java.util.List;
import java.util.Random;
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
    public void prepareDataBases(){
        createKeyspace();
        createPasswordTable();
        splitPassword("");
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
    public double calculateEntropy(String password){
        int passwordLength = password.length();
        int numberOfLowerCaseCharacters = 0;
        int numberOfUpperCaseCharacters = 0;
        int numberOfNumbers = 0;
        int numberOfSpecialSymbols = 0;

        for(int i = 0; i < passwordLength; i++){
            char c = password.charAt(i);
            if(Character.isLowerCase(c)){
                numberOfLowerCaseCharacters++;
            }
            else if(Character.isUpperCase(c)){
                numberOfUpperCaseCharacters++;
            }
            else if(Character.isDigit(c)){
                numberOfNumbers++;
            }
            else{
                numberOfSpecialSymbols++;
            }
        }

        int possibleCharacters = numberOfLowerCaseCharacters + numberOfUpperCaseCharacters
                + numberOfNumbers + numberOfSpecialSymbols;

        double entropy = Math.log(possibleCharacters) / Math.log(2) * passwordLength;

        return entropy;
    }

    public void savePasswordHalf(){
        Password password = new Password();
        password.setHash("asfgqbuwjb19gwbd917v237a7sd");
//        passwordRepository.save(password);
        String createTableCql = "CREATE TABLE IF NOT EXISTS user_passwords.password (" + "id UUID PRIMARY KEY, " + "hash text" + ")";
        SimpleStatement statement = SimpleStatement.builder(createTableCql).build();
        cassandraTemplate.execute(statement);
        UUID id = UUID.randomUUID();
        String hash = password.getHash();
        String insertCql = "INSERT INTO user_passwords.password (id, hash) VALUES (?, ?)";
        SimpleStatement insertPassword = SimpleStatement.builder(insertCql).addPositionalValues(id,hash).build();
        cassandraTemplate.execute(insertPassword);
        String hash2 = password.getHash();
        String insertCql2 = "INSERT INTO user_passwords.password (id, hash) VALUES (?, ?)";
        SimpleStatement insertPassword2 = SimpleStatement.builder(insertCql).addPositionalValues(id,hash2).build();
        db2CassandraTemplate2.execute(statement);
        db2CassandraTemplate2.execute(insertPassword);
    }

    public String reconstructPassword(UUID id){
        String selectCql = "SELECT * FROM user_passwords.password WHERE id = ?";
        SimpleStatement statement = SimpleStatement.builder(selectCql).addPositionalValue(id).build();
        ResultSet resultSetDb1 = cassandraTemplate.execute(statement);
        String passwordFirstPart = resultSetDb1.one().getString("hash");
        ResultSet resultSetDb2 = db2CassandraTemplate2.execute(statement);
        String passwordSecondPart = resultSetDb2.one().getString("hash");
        return passwordFirstPart + passwordSecondPart;
    }
    public void splitPassword(String password){
        UUID test = UUID.randomUUID();
        password = "12345678";
        String firstPart = password.substring(0,password.length()/2);
        String secondPart = password.substring(password.length()/2);
        writeToDb1(firstPart,test);
        writeToDb2(secondPart,test);
    }

    public void writeToDb1(String passwordHash, UUID id){
        String insertCql = "INSERT INTO user_passwords.password (id, hash) VALUES (?, ?)";
        SimpleStatement insertPassword = SimpleStatement.builder(insertCql).addPositionalValues(id,passwordHash).build();
        cassandraTemplate.execute(insertPassword);
    }
    public void writeToDb2(String passwordHashSecondPart, UUID id){
        String insertCql = "INSERT INTO user_passwords.password (id, hash) VALUES (?, ?)";
        SimpleStatement insertPassword = SimpleStatement.builder(insertCql).addPositionalValues(id,passwordHashSecondPart).build();
        db2CassandraTemplate2.execute(insertPassword);
    }
    public void createKeyspace(){
        String createKeyspace = "CREATE KEYSPACE IF NOT EXISTS user_passwords\n" +
                "  WITH REPLICATION = { \n" +
                "   'class' : 'SimpleStrategy', \n" +
                "   'replication_factor' : 1 \n" +
                "  };";
        SimpleStatement statement = SimpleStatement.builder(createKeyspace).build();
        cassandraTemplate.execute(statement);
        db2CassandraTemplate2.execute(statement);


    }
    public void createPasswordTable(){
        String createTableCql = "CREATE TABLE IF NOT EXISTS user_passwords.password (" + "id UUID PRIMARY KEY, " + "hash text" + ")";
        SimpleStatement statement = SimpleStatement.builder(createTableCql).build();
        cassandraTemplate.execute(statement);
        db2CassandraTemplate2.execute(statement);
    }



}
