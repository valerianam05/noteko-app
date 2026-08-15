package com.example.demo.repository;

import com.example.demo.entity.AcademicYear;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, UUID> {

  Optional<AcademicYear> findByIsCurrentTrue();

  Optional<AcademicYear> findByLabel(String label);
}
