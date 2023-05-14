package com.SemesterProject.kmzi.service;

import org.springframework.stereotype.Service;

import me.legrange.haveibeenpwned.*;
@Service
public class HIBPService {

    private static String apiKey = "27dc294bf393424596485a06a938f6a2";


    public boolean checkIfEmailWasCompromised(String email){
        HaveIBeenPwndApi hibp = HaveIBeenPwndBuilder.create("MarioSchoolProject").withApiKey(apiKey).build();
        boolean isCompromised = false;
        try {
            isCompromised = hibp.isAccountPwned(email);
        } catch (HaveIBeenPwndException e) {
            e.printStackTrace();
        }
        return isCompromised;
    }
    public boolean checkIfPasswordHashWasInABreach(String password){
        HaveIBeenPwndApi hibp = HaveIBeenPwndBuilder.create("MarioSchoolProject").withApiKey(apiKey).build();
        boolean isCompromised = false;
        try {
            isCompromised = hibp.isHashPasswordPwned(password);
        } catch (HaveIBeenPwndException e) {
            e.printStackTrace();
        }
        return isCompromised;
    }


}
