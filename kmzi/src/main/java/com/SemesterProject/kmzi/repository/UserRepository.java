package com.SemesterProject.kmzi.repository;

import com.SemesterProject.kmzi.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
}
