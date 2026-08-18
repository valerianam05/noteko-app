package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.Student;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
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
class StudentServiceTest {

  @Mock private StudentRepository studentRepository;

  @InjectMocks private StudentService studentService;

  private UUID studentId;
  private String stdNumber;
  private Student student;

  @BeforeEach
  void setUp() {
    studentId = UUID.randomUUID();
    stdNumber = "STD21001";
    student = Student.builder().userId(studentId).stdNumber(stdNumber).build();
  }

  @Test
  @DisplayName("findByStdNumber doit retourner l'étudiant s'il existe")
  void findByStdNumber_Success() {
    when(studentRepository.findByStdNumber(stdNumber)).thenReturn(Optional.of(student));

    Student result = studentService.findByStdNumber(stdNumber);

    assertNotNull(result);
    assertEquals(stdNumber, result.getStdNumber());
    verify(studentRepository, times(1)).findByStdNumber(stdNumber);
  }

  @Test
  @DisplayName("findByStdNumber doit lever ResourceNotFoundException si introuvable")
  void findByStdNumber_NotFound() {
    when(studentRepository.findByStdNumber("STD99999")).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> studentService.findByStdNumber("STD99999"));
    verify(studentRepository, times(1)).findByStdNumber("STD99999");
  }

  @Test
  @DisplayName("findById doit retourner l'étudiant si l'ID existe")
  void findById_Success() {
    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    Student result = studentService.findById(studentId);

    assertNotNull(result);
    verify(studentRepository, times(1)).findById(studentId);
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si ID introuvable")
  void findById_NotFound() {
    UUID unknownId = UUID.randomUUID();
    when(studentRepository.findById(unknownId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> studentService.findById(unknownId));
  }
}
