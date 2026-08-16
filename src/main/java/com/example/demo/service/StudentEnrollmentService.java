package com.example.demo.service;

import com.example.demo.entity.AcademicGroup;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.Semester;
import com.example.demo.entity.Student;
import com.example.demo.entity.StudentEnrollment;
import com.example.demo.exception.ConflictException;
import com.example.demo.repository.StudentEnrollmentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentEnrollmentService {

  private final StudentEnrollmentRepository studentEnrollmentRepository;
  private final StudentService studentService;
  private final AcademicGroupService academicGroupService;
  private final SemesterService semesterService;
  private final AcademicYearService academicYearService;

  public List<StudentEnrollment> findByStudent(UUID studentId) {
    return studentEnrollmentRepository.findByStudentUserIdOrderByAcademicYearIdAsc(studentId);
  }

  public StudentEnrollment enroll(
      UUID studentId, UUID groupId, UUID semesterId, UUID academicYearId) {
    studentEnrollmentRepository
        .findByStudentUserIdAndSemesterIdAndAcademicYearId(studentId, semesterId, academicYearId)
        .ifPresent(
            existing -> {
              throw new ConflictException(
                  "Cet étudiant est déjà inscrit pour ce semestre et cette année universitaire");
            });

    Student student = studentService.findById(studentId);
    AcademicGroup group = academicGroupService.findById(groupId);
    Semester semester = semesterService.findById(semesterId);
    AcademicYear academicYear = academicYearService.findById(academicYearId);

    fermerInscriptionPrecedente(studentId);

    StudentEnrollment enrollment =
        StudentEnrollment.builder()
            .student(student)
            .group(group)
            .semester(semester)
            .academicYear(academicYear)
            .dateDebut(LocalDate.now())
            .build();
    return studentEnrollmentRepository.save(enrollment);
  }

  private void fermerInscriptionPrecedente(UUID studentId) {
    studentEnrollmentRepository
        .findFirstByStudentUserIdAndDateFinIsNullOrderByDateDebutDesc(studentId)
        .ifPresent(
            precedente -> {
              precedente.setDateFin(LocalDate.now());
              studentEnrollmentRepository.save(precedente);
            });
  }
}
