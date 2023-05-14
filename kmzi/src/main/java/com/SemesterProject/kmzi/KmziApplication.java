package com.SemesterProject.kmzi;

import com.SemesterProject.kmzi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication
@Configuration
@EnableJpaRepositories("com.SemesterProject.kmzi.*")
@ComponentScan(basePackages = { "com.SemesterProject.kmzi.*" })
@EntityScan("com.SemesterProject.kmzi.*")
public class KmziApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmziApplication.class, args);
	}
}
