package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.CourseRequest;
import com.example.demo.entity.Course;
import com.example.demo.entity.Semester;
import com.example.demo.entity.enums.Parcours;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.SemesterRepository;
import com.example.demo.service.CourseService;
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
class CourseServiceTest {

  @Mock private CourseRepository courseRepository;
  @Mock private SemesterRepository semesterRepository;

  @InjectMocks private CourseService courseService;

  private UUID courseId;
  private UUID semesterId;
  private Course course;
  private Semester semester;

  @BeforeEach
  void setUp() {
    courseId = UUID.randomUUID();
    semesterId = UUID.randomUUID();

    semester = Semester.builder().id(semesterId).build();

    course =
        Course.builder()
            .id(courseId)
            .code("INFO301")
            .title("Algorithmique avancée")
            .credits(4)
            .parcours(Parcours.COMMON)
            .semester(semester)
            .build();
  }

  @Test
  @DisplayName("findAll doit retourner la liste des cours")
  void findAll_Success() {
    when(courseRepository.findAll()).thenReturn(List.of(course));

    List<Course> result = courseService.findAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("INFO301", result.get(0).getCode());
  }

  @Test
  @DisplayName("findById doit retourner le cours si il existe")
  void findById_Success() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

    Course result = courseService.findById(courseId);

    assertNotNull(result);
    assertEquals("Algorithmique avancée", result.getTitle());
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si le cours n'existe pas")
  void findById_NotFound() {
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> courseService.findById(courseId));
  }

  @Test
  @DisplayName("findByParcours doit filtrer par parcours")
  void findByParcours_Success() {
    when(courseRepository.findByParcours(Parcours.COMMON)).thenReturn(List.of(course));

    List<Course> result = courseService.findByParcours("COMMON");

    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("findBySemester doit filtrer par semestre")
  void findBySemester_Success() {
    when(courseRepository.findBySemesterId(semesterId)).thenReturn(List.of(course));

    List<Course> result = courseService.findBySemester(semesterId);

    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("create doit créer le cours si le semestre existe")
  void create_Success() {
    CourseRequest request =
        new CourseRequest("INFO301", "Algorithmique avancée", 4, Parcours.COMMON, semesterId);

    when(semesterRepository.findById(semesterId)).thenReturn(Optional.of(semester));
    when(courseRepository.save(any(Course.class))).thenReturn(course);

    Course result = courseService.create(request);

    assertNotNull(result);
    assertEquals("INFO301", result.getCode());
    verify(courseRepository, times(1)).save(any(Course.class));
  }

  @Test
  @DisplayName("create doit lever ResourceNotFoundException si le semestre n'existe pas")
  void create_SemesterNotFound() {
    CourseRequest request =
        new CourseRequest("INFO301", "Algorithmique avancée", 4, Parcours.COMMON, semesterId);

    when(semesterRepository.findById(semesterId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> courseService.create(request));

    verify(courseRepository, never()).save(any(Course.class));
  }
}
