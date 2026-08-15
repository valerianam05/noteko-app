package com.example.demo.repository;

import com.example.demo.entity.Teacher;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {}
