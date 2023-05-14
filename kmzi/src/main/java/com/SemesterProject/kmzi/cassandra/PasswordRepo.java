package com.SemesterProject.kmzi.cassandra;

import com.SemesterProject.kmzi.entity.Password;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Qualifier("database1")
public interface PasswordRepo extends CassandraRepository<Password, UUID> {

}