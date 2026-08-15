package com.example.demo.repository;

import com.example.demo.entity.Student;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {

  Optional<Student> findByStdNumber(String stdNumber);

  boolean existsByStdNumber(String stdNumber);
}
