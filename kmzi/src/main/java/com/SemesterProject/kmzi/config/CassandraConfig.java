package com.SemesterProject.kmzi.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig {


    @Autowired
    private CassandraProperties cassandraProperties;


    @Autowired
    private Db2Properties db2Properties;


    @Bean
    public CqlSession session() {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(cassandraProperties.getContactPoints().get(0), 9042))
                .withKeyspace(cassandraProperties.getKeyspaceName())
                .withAuthCredentials(cassandraProperties.getUsername(), cassandraProperties.getPassword())
                .build();
    }


    @Bean
    public CqlSession db2Session() {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(db2Properties.getContactPoints(), 9042))
                .withKeyspace(db2Properties.getKeyspaceName())
                .withAuthCredentials(db2Properties.getUsername(), db2Properties.getPassword())
                .build();
    }


    @Bean
    public CassandraOperations cassandraTemplate() {
        return new CassandraTemplate(session());
    }


    @Bean
    public CassandraOperations db2CassandraTemplate() {
        return new CassandraTemplate(db2Session());
    }
}