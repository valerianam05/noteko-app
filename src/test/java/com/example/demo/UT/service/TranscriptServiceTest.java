package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.response.GraduationStatusResponse;
import com.example.demo.dto.response.TranscriptResponse;
import com.example.demo.entity.*;
import com.example.demo.entity.enums.AcademicLevel;
import com.example.demo.entity.enums.Parcours;
import com.example.demo.entity.enums.SemesterCode;
import com.example.demo.repository.CourseValidationRepository;
import com.example.demo.service.StudentService;
import com.example.demo.service.TranscriptService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TranscriptServiceTest {

  @Mock private StudentService studentService;
  @Mock private CourseValidationRepository courseValidationRepository;

  @InjectMocks private TranscriptService transcriptService;

  private String std;
  private Student student;
  private CourseValidation validation1;

  @BeforeEach
  void setUp() {
    std = "STD21001";
    UUID studentId = UUID.randomUUID();

    AppUser user = AppUser.builder().firstName("Valeria").lastName("Nam").build();

    AcademicGroup group = AcademicGroup.builder().parcours(Parcours.COMMON).build();

    StudentEnrollment enrollment = StudentEnrollment.builder().group(group).build();

    student =
        Student.builder()
            .userId(studentId)
            .stdNumber(std)
            .appUser(user)
            .enrollments(List.of(enrollment))
            .build();

    Semester semester = Semester.builder().code(SemesterCode.S1).level(AcademicLevel.L1).build();
    Course course = Course.builder().semester(semester).build();

    validation1 =
        CourseValidation.builder()
            .course(course)
            .finalAverage(12.0)
            .validated(true)
            .creditsObtained(60)
            .build();
  }

  @Test
  @DisplayName("getTranscript génère le relevé sans filtre de semestre")
  void getTranscript_WithoutSemester() {
    when(studentService.findByStdNumber(std)).thenReturn(student);
    when(courseValidationRepository.findByStudent_UserId(student.getId()))
        .thenReturn(List.of(validation1));

    TranscriptResponse response = transcriptService.getTranscript(std, null);

    assertNotNull(response);
    assertEquals("STD21001", response.studentStd());
    assertEquals("Valeria Nam", response.studentName());
    assertEquals(12.0, response.generalAverage());
    assertEquals(60, response.totalValidatedCredits());
    assertFalse(response.isGraduated());
  }

  @Test
  @DisplayName("getTranscript avec filtre semestre correspondant")
  void getTranscript_WithMatchingSemester() {
    when(studentService.findByStdNumber(std)).thenReturn(student);
    when(courseValidationRepository.findByStudent_UserId(student.getId()))
        .thenReturn(List.of(validation1));

    TranscriptResponse response = transcriptService.getTranscript(std, "S1");

    assertNotNull(response);
    assertEquals(12.0, response.generalAverage());
  }

  @Test
  @DisplayName("getGraduationStatus calcule le statut par niveau L1/L2/L3")
  void getGraduationStatus_Success() {
    when(studentService.findByStdNumber(std)).thenReturn(student);
    when(courseValidationRepository.findByStudent_UserId(student.getId()))
        .thenReturn(List.of(validation1));

    GraduationStatusResponse response = transcriptService.getGraduationStatus(std);

    assertNotNull(response);
    assertEquals(180, response.totalRequiredCredits());
    assertEquals(60, response.totalValidatedCredits());
    assertTrue(response.creditsByLevel().containsKey("L1"));
    assertEquals(60, response.creditsByLevel().get("L1"));
    assertFalse(response.isGraduated());
  }
}
