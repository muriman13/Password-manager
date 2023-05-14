package com.SemesterProject.kmzi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class PasswordDistributionService {

//    @Autowired
//    @Qualifier("db1")
//    private DataSource dataSource1;
//
//    @Autowired
//    @Qualifier("db2")
//    private DataSource dataSource2;
//
//    @Bean(name = "db1JdbcTemplate")
//    public JdbcTemplate db1JdbcTemplate() {
//        return new JdbcTemplate(dataSource1);
//    }
//
//    @Bean(name = "db2JdbcTemplate")
//    public JdbcTemplate db2JdbcTemplate() {
//        return new JdbcTemplate(dataSource2);
//    }
//    public void splitPassword(){
//
//    }
//    public void retrievePassword(){
//
//    }
}
