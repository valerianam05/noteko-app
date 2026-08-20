package com.example.demo.UT.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.endpoint.rest.controller.grade.CourseController;
import com.example.demo.entity.Course;
import com.example.demo.entity.Semester;
import com.example.demo.entity.enums.Parcours;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// addFilters = false désactive les filtres Spring Security pour ce test : on teste la
// LOGIQUE du contrôleur, pas l'authentification (qui devrait être testée séparément)
@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private CourseService courseService;

  // Même avec addFilters = false, @WebMvcTest scanne et essaie de CONSTRUIRE tous les
  // beans Filter du projet (dont BearerAuthFilter), qui a besoin d'un JwtService dans
  // son constructeur. On simule donc JwtService pour satisfaire cette dépendance.
  @MockBean private JwtService jwtService;

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
  @DisplayName("GET /api/courses doit retourner la liste des cours")
  void getCourses_Success() throws Exception {
    when(courseService.findAll()).thenReturn(List.of(course));

    mockMvc
        .perform(get("/api/courses"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].code").value("INFO301"))
        .andExpect(jsonPath("$[0].credits").value(4));
  }

  @Test
  @DisplayName("GET /api/courses/{id} doit retourner le cours demandé")
  void getCourseById_Success() throws Exception {
    when(courseService.findById(courseId)).thenReturn(course);

    mockMvc
        .perform(get("/api/courses/{courseId}", courseId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(courseId.toString()))
        .andExpect(jsonPath("$.title").value("Algorithmique avancée"));
  }

  @Test
  @DisplayName("GET /api/courses/{id} doit retourner 404 si le cours n'existe pas")
  void getCourseById_NotFound() throws Exception {
    when(courseService.findById(courseId))
        .thenThrow(new ResourceNotFoundException("Cours introuvable avec l'ID : " + courseId));

    mockMvc.perform(get("/api/courses/{courseId}", courseId)).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("POST /api/courses doit créer un cours et retourner 201")
  void createCourse_Success() throws Exception {
    when(courseService.create(any())).thenReturn(course);

    String requestBody =
        """
        {
          "code": "INFO301",
          "title": "Algorithmique avancée",
          "credits": 4,
          "parcours": "COMMON",
          "semesterId": "%s"
        }
        """
            .formatted(semesterId);

    mockMvc
        .perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value("INFO301"))
        .andExpect(jsonPath("$.credits").value(4));
  }

  @Test
  @DisplayName("POST /api/courses doit retourner 400 si le champ 'code' est manquant")
  void createCourse_ValidationError() throws Exception {
    String requestBody =
        """
        {
          "title": "Algorithmique avancée",
          "credits": 4,
          "parcours": "COMMON",
          "semesterId": "%s"
        }
        """
            .formatted(semesterId);

    mockMvc
        .perform(post("/api/courses").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest());
  }
}
