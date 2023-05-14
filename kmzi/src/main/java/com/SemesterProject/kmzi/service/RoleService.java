package com.SemesterProject.kmzi.service;

import com.SemesterProject.kmzi.entity.Role;
import com.SemesterProject.kmzi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public void save(Role role){
        roleRepository.save(role);
    }

    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
