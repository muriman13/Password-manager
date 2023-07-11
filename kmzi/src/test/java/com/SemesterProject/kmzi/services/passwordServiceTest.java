package com.SemesterProject.kmzi.services;

import com.SemesterProject.kmzi.service.PasswordService;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class passwordServiceTest {


    private PasswordService passwordService = new PasswordService();

    @Test
    public void testCreateRulesForPassword() {
        String password = passwordService.createRulesForPassword();
        // Perform assertions or validations on the generated password
        Assertions.assertNotNull(password);
        Assertions.assertEquals(24, password.length());
        // Add more assertions if needed
    }

    @Test
    public void testCalculateEntropy() {
        String password = "examplePassword123!";
        double entropy = passwordService.calculateEntropy(password);
        Assertions.assertEquals(80.71062275542812, entropy, 0);
    }

    @Test
    public void testDecryptPassword() {
        String encryptedPassword = "encryptedPasswordsDjagaraDjugara";
        String secretKey = "keyThatIs16symbo";
        String decryptedPassword = passwordService.decryptPassword(encryptedPassword, secretKey);
        //empty is ok, it should never be null
        Assertions.assertNotNull(decryptedPassword);
        // Add more assertions if needed
    }
}
