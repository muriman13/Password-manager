package com.SemesterProject.kmzi.config;

import com.SemesterProject.kmzi.entity.Role;
import com.SemesterProject.kmzi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;

    @Autowired
    public DataInitializer(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleService.findByName(roleName) == null) {
            Role role = new Role();
            role.setName(roleName);
            roleService.save(role);
        }
    }
}

