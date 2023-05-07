package com.SemesterProject.kmzi;

import com.SemesterProject.kmzi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class KmziApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmziApplication.class, args);
	}

}
