package com.SemesterProject.kmzi.controller;

import com.SemesterProject.kmzi.entity.Credential;
import com.SemesterProject.kmzi.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CredentialsController {

    @Autowired
    private CredentialRepository credentialRepository;

    @PostMapping("/saveCredential")
    public String saveCredential(@ModelAttribute("credential") Credential credential) {
        credentialRepository.save(credential);
        // save the credential object in database
        return "redirect:/credentials";
    }

}
