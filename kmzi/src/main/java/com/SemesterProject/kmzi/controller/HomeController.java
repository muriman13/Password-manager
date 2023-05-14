package com.SemesterProject.kmzi.controller;

import com.SemesterProject.kmzi.entity.Credential;
import com.SemesterProject.kmzi.entity.Message;
import com.SemesterProject.kmzi.entity.User;
import com.SemesterProject.kmzi.repository.MessageRepository;
import com.SemesterProject.kmzi.service.CredentialService;
import com.SemesterProject.kmzi.service.PasswordService;
import com.SemesterProject.kmzi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private UserService userService;
    @Autowired
    private CredentialService credentialService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("msgs", messageRepository.findAll());
        return "userhome";
    }
    @GetMapping("/create-credentials")
    public String createCredentials(Model model){
        passwordService.savePasswordHalf();
        String generatedPassword = passwordService.createRulesForPassword();
        model.addAttribute("credential", new Credential());
        model.addAttribute("password", generatedPassword);
        model.addAttribute("entropy", passwordService.calculateEntropy(generatedPassword));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userService.findByUsername(username);
            model.addAttribute("user_id", user.getId());

        }
        List<Credential> credentials = credentialService.getAllCredentials();
        model.addAttribute("credentialList", credentials);
        return "create-password";
    }

    @PostMapping("/messages")
    public String saveMessage(Message message) {
        messageRepository.save(message);
        return "redirect:/home";
    }
}