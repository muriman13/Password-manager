package com.SemesterProject.kmzi.service;



import com.SemesterProject.kmzi.entity.Role;
import com.SemesterProject.kmzi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.SemesterProject.kmzi.repository.UserRepository;

import java.util.Arrays;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createAdminUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(List.of(new Role(), new Role()));
        userRepository.save(user);
    }
}

