package com.SemesterProject.kmzi.repository;

import com.SemesterProject.kmzi.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Ramesh Fadatare
 *
 */
public interface MessageRepository extends JpaRepository<Message, Integer>{

}
