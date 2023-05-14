package com.SemesterProject.kmzi.controller;

import com.SemesterProject.kmzi.entity.Role;
import com.SemesterProject.kmzi.entity.User;
import com.SemesterProject.kmzi.service.RoleService;
import com.SemesterProject.kmzi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute("user") @Valid User user,
                                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registration";
        }
        // Check if username or email already exists in database
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "Username already exists");
            return "registration";
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Email already exists");
            return "registration";
        }
        // Hash the password before saving it to the database
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        // Add the user role by default
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleService.save(userRole);
        user.setRoles(Arrays.asList(userRole));

        userService.save(user);

        return "redirect:/login?success";
    }
}
