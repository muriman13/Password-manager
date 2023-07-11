package com.SemesterProject.kmzi.services;

import com.SemesterProject.kmzi.service.HIBPService;
import me.legrange.haveibeenpwned.HaveIBeenPwndApi;
import me.legrange.haveibeenpwned.HaveIBeenPwndBuilder;
import me.legrange.haveibeenpwned.HaveIBeenPwndException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HIBPServiceTest {

    private static String apiKey = "27dc294bf393424596485a06a938f6a2";

    HaveIBeenPwndApi hibp = HaveIBeenPwndBuilder.create("MarioSchoolProject").withApiKey(apiKey).build();
    private HIBPService HIBPservice = new HIBPService();


    @Test
    public void testCheckIfEmailWasCompromised() throws HaveIBeenPwndException {
        String email = "test@example.com";
        boolean isCompromised = true;

        Mockito.when(hibp.isAccountPwned(email)).thenReturn(isCompromised);

        boolean result = HIBPservice.checkIfEmailWasCompromised(email);

        Assertions.assertTrue(result);
        Mockito.verify(hibp).isAccountPwned(email);
    }

    @Test
    public void testCheckIfPasswordHashWasInABreach() throws HaveIBeenPwndException {
        String password = "password123";
        boolean isCompromised = true;

        Mockito.when(hibp.isHashPasswordPwned(password)).thenReturn(isCompromised);

        boolean result = HIBPservice.checkIfPasswordHashWasInABreach(password);

        Assertions.assertTrue(result);
        Mockito.verify(hibp).isHashPasswordPwned(password);
    }

    @Test
    public void testCheckIfPasswordHashWasNotInABreach() {
        String password = "password123";
        boolean isCompromised = false;

        try {
            Mockito.when(hibp.isHashPasswordPwned(password)).thenReturn(isCompromised);
        } catch (HaveIBeenPwndException e) {
            throw new RuntimeException(e);
        }

        boolean result = HIBPservice.checkIfPasswordHashWasInABreach(password);

        Assertions.assertFalse(result);
        try {
            Mockito.verify(hibp).isHashPasswordPwned(password);
        } catch (HaveIBeenPwndException e) {
            Assertions.assertTrue(true);
        }
    }


}
