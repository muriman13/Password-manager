package com.SemesterProject.kmzi.controller;

import com.SemesterProject.kmzi.service.HIBPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("")
public class HIBPController {

    @Autowired
    private HIBPService hibpService;

    @GetMapping("/check-email")
    public boolean checkIfAccountIsPwned(@RequestParam String email){
        return hibpService.checkIfEmailWasCompromised(email);
    }
    @GetMapping("/check-password")
    public boolean checkIfPasswordHashWasInABreach(@RequestParam String password){
        return hibpService.checkIfPasswordHashWasInABreach(password);
    }
}
