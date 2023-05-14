package com.SemesterProject.kmzi.repository;

import com.SemesterProject.kmzi.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    List<Credential> findAll();
    @Override
    @Transactional
    <S extends Credential> S save(S entity);
}
