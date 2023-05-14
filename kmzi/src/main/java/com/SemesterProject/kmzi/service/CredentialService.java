package com.SemesterProject.kmzi.service;

import com.SemesterProject.kmzi.entity.Credential;
import com.SemesterProject.kmzi.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    @Autowired
    CredentialRepository credentialRepository;

    public List<Credential> getAllCredentials(){
        return credentialRepository.findAll();
    }
}
