package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.CourseValidationRequest;
import com.example.demo.dto.response.CourseValidationResponse;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.Course;
import com.example.demo.entity.CourseValidation;
import com.example.demo.entity.Student;
import com.example.demo.entity.enums.SessionType;
import com.example.demo.repository.CourseValidationRepository;
import com.example.demo.service.AcademicYearService;
import com.example.demo.service.CourseService;
import com.example.demo.service.CourseValidationService;
import com.example.demo.service.StudentService;
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
class CourseValidationServiceTest {

  @Mock private CourseValidationRepository courseValidationRepository;
  @Mock private StudentService studentService;
  @Mock private CourseService courseService;
  @Mock private AcademicYearService academicYearService;

  @InjectMocks private CourseValidationService courseValidationService;

  private UUID studentId;
  private UUID academicYearId;
  private UUID courseId;
  private Student student;
  private Course course;
  private AcademicYear academicYear;
  private CourseValidation courseValidation;

  @BeforeEach
  void setUp() {
    studentId = UUID.randomUUID();
    academicYearId = UUID.randomUUID();
    courseId = UUID.randomUUID();

    student = Student.builder().userId(studentId).stdNumber("STD21001").build();
    course = Course.builder().id(courseId).code("PROG2").credits(6).build();
    academicYear = AcademicYear.builder().id(academicYearId).build();

    courseValidation =
        CourseValidation.builder()
            .id(UUID.randomUUID())
            .student(student)
            .course(course)
            .academicYear(academicYear)
            .finalAverage(14.5)
            .validated(true)
            .creditsObtained(6)
            .session(SessionType.NORMAL)
            .build();
  }

  @Test
  @DisplayName("getCourseValidations avec studentId et academicYearId non nuls")
  void getCourseValidations_WithBothIds() {
    when(courseValidationRepository.findByStudent_UserIdAndAcademicYear_Id(
            studentId, academicYearId))
        .thenReturn(List.of(courseValidation));

    List<CourseValidationResponse> result =
        courseValidationService.getCourseValidations(studentId, academicYearId);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(courseValidationRepository, times(1))
        .findByStudent_UserIdAndAcademicYear_Id(studentId, academicYearId);
  }

  @Test
  @DisplayName("getCourseValidations avec seulement studentId non nul")
  void getCourseValidations_WithStudentIdOnly() {
    when(courseValidationRepository.findByStudent_UserId(studentId))
        .thenReturn(List.of(courseValidation));

    List<CourseValidationResponse> result =
        courseValidationService.getCourseValidations(studentId, null);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(courseValidationRepository, times(1)).findByStudent_UserId(studentId);
  }

  @Test
  @DisplayName("getCourseValidations sans filtres (retourne tout)")
  void getCourseValidations_WithNoIds() {
    when(courseValidationRepository.findAll()).thenReturn(List.of(courseValidation));

    List<CourseValidationResponse> result =
        courseValidationService.getCourseValidations(null, null);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(courseValidationRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("computeCourseValidation calcule et sauvegarde une nouvelle validation")
  void computeCourseValidation_Success() {
    CourseValidationRequest request =
        new CourseValidationRequest(studentId, courseId, academicYearId, 15.0, true, 6, "NORMAL");

    when(studentService.findById(studentId)).thenReturn(student);
    when(courseService.findById(courseId)).thenReturn(course);
    when(academicYearService.findById(academicYearId)).thenReturn(academicYear);
    when(courseValidationRepository.findByStudent_UserIdAndCourse_IdAndAcademicYear_Id(
            studentId, courseId, academicYearId))
        .thenReturn(Optional.empty());
    when(courseValidationRepository.save(any(CourseValidation.class))).thenReturn(courseValidation);

    CourseValidationResponse response = courseValidationService.computeCourseValidation(request);

    assertNotNull(response);
    verify(courseValidationRepository, times(1)).save(any(CourseValidation.class));
  }
}
