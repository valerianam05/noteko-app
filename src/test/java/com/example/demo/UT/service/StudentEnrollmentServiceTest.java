package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.entity.AcademicGroup;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.Semester;
import com.example.demo.entity.Student;
import com.example.demo.entity.StudentEnrollment;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.StudentEnrollmentRepository;
import com.example.demo.service.AcademicGroupService;
import com.example.demo.service.AcademicYearService;
import com.example.demo.service.SemesterService;
import com.example.demo.service.StudentEnrollmentService;
import com.example.demo.service.StudentService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentEnrollmentServiceTest {

  @Mock private StudentEnrollmentRepository studentEnrollmentRepository;
  @Mock private StudentService studentService;
  @Mock private AcademicGroupService academicGroupService;
  @Mock private SemesterService semesterService;
  @Mock private AcademicYearService academicYearService;

  @InjectMocks private StudentEnrollmentService studentEnrollmentService;

  private UUID studentId;
  private UUID groupId;
  private UUID semesterId;
  private UUID academicYearId;
  private UUID enrollmentId;

  private Student student;
  private AcademicGroup group;
  private Semester semester;
  private AcademicYear academicYear;
  private StudentEnrollment enrollment;

  @BeforeEach
  void setUp() {
    studentId = UUID.randomUUID();
    groupId = UUID.randomUUID();
    semesterId = UUID.randomUUID();
    academicYearId = UUID.randomUUID();
    enrollmentId = UUID.randomUUID();

    student = Student.builder().userId(studentId).build();
    group = AcademicGroup.builder().id(groupId).build();
    semester = Semester.builder().id(semesterId).build();
    academicYear = AcademicYear.builder().id(academicYearId).build();

    enrollment =
        StudentEnrollment.builder()
            .id(enrollmentId)
            .student(student)
            .group(group)
            .semester(semester)
            .academicYear(academicYear)
            .dateDebut(LocalDate.now())
            .build();
  }

  @Test
  @DisplayName("findByStudent doit retourner la liste des inscriptions d'un étudiant")
  void findByStudent_Success() {
    when(studentEnrollmentRepository.findByStudentUserIdOrderByAcademicYearIdAsc(studentId))
        .thenReturn(List.of(enrollment));

    List<StudentEnrollment> results = studentEnrollmentService.findByStudent(studentId);

    assertNotNull(results);
    assertEquals(1, results.size());
    verify(studentEnrollmentRepository, times(1))
        .findByStudentUserIdOrderByAcademicYearIdAsc(studentId);
  }

  @Test
  @DisplayName("findActiveByGroup doit retourner les inscriptions actives du groupe")
  void findActiveByGroup_Success() {
    when(studentEnrollmentRepository.findByGroupIdAndDateFinIsNull(groupId))
        .thenReturn(List.of(enrollment));

    List<StudentEnrollment> results = studentEnrollmentService.findActiveByGroup(groupId);

    assertNotNull(results);
    assertEquals(1, results.size());
    verify(studentEnrollmentRepository, times(1)).findByGroupIdAndDateFinIsNull(groupId);
  }

  @Test
  @DisplayName("enroll doit lever ConflictException si l'étudiant est déjà inscrit")
  void enroll_ConflictException() {
    when(studentEnrollmentRepository.findByStudentUserIdAndSemesterIdAndAcademicYearId(
            studentId, semesterId, academicYearId))
        .thenReturn(Optional.of(enrollment));

    assertThrows(
        ConflictException.class,
        () -> studentEnrollmentService.enroll(studentId, groupId, semesterId, academicYearId));

    verify(studentEnrollmentRepository, never()).save(any());
  }

  @Test
  @DisplayName("enroll doit créer une inscription et fermer la précédente si elle existe")
  void enroll_Success_WithPreviousEnrollment() {
    StudentEnrollment previousEnrollment =
        StudentEnrollment.builder().id(UUID.randomUUID()).student(student).build();

    when(studentEnrollmentRepository.findByStudentUserIdAndSemesterIdAndAcademicYearId(
            studentId, semesterId, academicYearId))
        .thenReturn(Optional.empty());

    when(studentService.findById(studentId)).thenReturn(student);
    when(academicGroupService.findById(groupId)).thenReturn(group);
    when(semesterService.findById(semesterId)).thenReturn(semester);
    when(academicYearService.findById(academicYearId)).thenReturn(academicYear);

    when(studentEnrollmentRepository.findFirstByStudentUserIdAndDateFinIsNullOrderByDateDebutDesc(
            studentId))
        .thenReturn(Optional.of(previousEnrollment));

    when(studentEnrollmentRepository.save(any(StudentEnrollment.class))).thenReturn(enrollment);

    StudentEnrollment result =
        studentEnrollmentService.enroll(studentId, groupId, semesterId, academicYearId);

    assertNotNull(result);
    assertNotNull(previousEnrollment.getDateFin());
    verify(studentEnrollmentRepository, times(2)).save(any(StudentEnrollment.class));
  }

  @Test
  @DisplayName("close doit fermer l'inscription si elle existe")
  void close_Success() {
    LocalDate dateFin = LocalDate.now();
    when(studentEnrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
    when(studentEnrollmentRepository.save(enrollment)).thenReturn(enrollment);

    StudentEnrollment result = studentEnrollmentService.close(enrollmentId, dateFin);

    assertNotNull(result);
    assertEquals(dateFin, result.getDateFin());
    verify(studentEnrollmentRepository, times(1)).save(enrollment);
  }

  @Test
  @DisplayName("close doit lever ResourceNotFoundException si l'inscription n'existe pas")
  void close_NotFound() {
    LocalDate dateFin = LocalDate.now();
    when(studentEnrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> studentEnrollmentService.close(enrollmentId, dateFin));

    verify(studentEnrollmentRepository, never()).save(any());
  }
}
