//package com.SemesterProject.kmzi.config;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.TransactionManagementConfigurer;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//@Configuration
//@PropertySource({"classpath:application.properties"})
//@EnableJpaRepositories(
//        basePackages = "com.SemesterProject.kmzi.entity.*",
//        entityManagerFactoryRef = "passwordEntityManager",
//        transactionManagerRef = "passwordTransactionManager")
//public class PersistenceCredentialAutoConfiguration {
//
//    @Bean
//    @ConfigurationProperties(prefix="spring.second-datasource")
//    public DataSource productDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean passwordEntityManager(EntityManagerFactoryBuilder builder, DataSource productDataSource) {
//        return builder
//                .dataSource(productDataSource)
//                .packages("com.SemesterProject.kmzi.db1") // Replace with your entity package
//                .persistenceUnit("passwordEntityManager")
//                .build();
//    }
//
//    @Bean
//    public PlatformTransactionManager passwordTransactionManager(EntityManagerFactory passwordEntityManager) {
//        return new JpaTransactionManager(passwordEntityManager);
//    }
//}
