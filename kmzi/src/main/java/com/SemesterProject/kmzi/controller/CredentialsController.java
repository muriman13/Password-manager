package com.SemesterProject.kmzi.controller;

import com.SemesterProject.kmzi.entity.Credential;
import com.SemesterProject.kmzi.entity.User;
import com.SemesterProject.kmzi.repository.CredentialRepository;
import com.SemesterProject.kmzi.service.CredentialService;
import com.SemesterProject.kmzi.service.PasswordService;
import com.SemesterProject.kmzi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CredentialsController {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/saveCredential")
    public String saveCredential(@ModelAttribute("credential") Credential credential) {

//        passwordService.savePassword();
//        credential.getPassword();
//        credential.getId();
        Credential savedCredential =  credentialRepository.save(credential);
        passwordService.splitPassword(savedCredential.getPassword(), Math.toIntExact(savedCredential.getId()));
        // save the credential object in database
        return "redirect:/credentials";
    }
    @GetMapping("/view-credentials")
    public String viewCredentials(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userService.findByUsername(username);
            model.addAttribute("user_id", user.getId());
        }
        List<Credential> credentials = credentialService.getAllCredentials(Integer.valueOf(model.getAttribute("user_id").toString()));
        model.addAttribute("credentialList", credentials);
        return "credentials";
    }

    @GetMapping("/get-decrypted-password")
    @ResponseBody
    public String getDecryptedPassword(@RequestParam String id, @RequestParam String secret){
        return passwordService.getDecryptedPassword(secret, id);
    }
}
