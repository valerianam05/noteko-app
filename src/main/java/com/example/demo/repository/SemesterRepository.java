package com.example.demo.repository;

import com.example.demo.entity.Semester;
import com.example.demo.entity.enums.SemesterCode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, UUID> {

  Optional<Semester> findByCode(SemesterCode code);

  List<Semester> findAllByOrderByOrderNumAsc();
}
