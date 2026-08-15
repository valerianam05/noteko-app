package com.example.demo.repository;

import com.example.demo.entity.DocumentExport;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentExportRepository extends JpaRepository<DocumentExport, UUID> {
  List<DocumentExport> findByStudentUserId(UUID studentId);

  List<DocumentExport> findByPromotion_Id(UUID promotionId);

  List<DocumentExport> findByAcademicYear_Id(UUID academicYearId);
}
