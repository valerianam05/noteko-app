package com.example.demo.UT.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.dto.response.StudentResponse;
import com.example.demo.endpoint.rest.controller.grade.StudentController;
import com.example.demo.entity.Student;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.security.filter.BearerAuthFilter;
import com.example.demo.service.StudentService;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = StudentController.class,
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BearerAuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean private StudentService studentService;

    // StudentController injecte StudentMapper comme un bean (pas des méthodes statiques
    // comme CourseMapper/GradeMapper), il faut donc le mocker aussi.
    @MockBean private StudentMapper studentMapper;

    private UUID studentId;
    private Student student;
    private StudentResponse studentResponse;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();

        student = Student.builder().userId(studentId).stdNumber("STD24020").build();

        studentResponse =
                new StudentResponse(studentId, "Henintsoa Maminiaina", "henintsoa@gmail.com", "STD24020");
    }

    @Test
    @DisplayName("GET /api/students doit retourner la liste des étudiants")
    void list_Success() throws Exception {
        when(studentService.findAll()).thenReturn(List.of(student));
        when(studentMapper.toResponse(student)).thenReturn(studentResponse);

        mockMvc
                .perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].stdNumber").value("STD24020"));
    }

    @Test
    @DisplayName("GET /api/students/{id} doit retourner l'étudiant demandé")
    void detail_Success() throws Exception {
        when(studentService.findById(studentId)).thenReturn(student);
        when(studentMapper.toResponse(student)).thenReturn(studentResponse);

        mockMvc
                .perform(get("/api/students/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("henintsoa@gmail.com"));
    }

    @Test
    @DisplayName("GET /api/students/{id} doit retourner 404 si l'étudiant n'existe pas")
    void detail_NotFound() throws Exception {
        when(studentService.findById(studentId))
                .thenThrow(new ResourceNotFoundException("Étudiant introuvable : " + studentId));

        mockMvc.perform(get("/api/students/{id}", studentId)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/students/by-std/{std} doit retourner l'étudiant par matricule")
    void detailByStd_Success() throws Exception {
        when(studentService.findByStdNumber("STD24020")).thenReturn(student);
        when(studentMapper.toResponse(student)).thenReturn(studentResponse);

        mockMvc
                .perform(get("/api/students/by-std/{std}", "STD24020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stdNumber").value("STD24020"));
    }

    @Test
    @DisplayName("GET /api/students/by-std/{std} doit retourner 404 si matricule inconnu")
    void detailByStd_NotFound() throws Exception {
        when(studentService.findByStdNumber("UNKNOWN"))
                .thenThrow(new ResourceNotFoundException("Étudiant introuvable, matricule : UNKNOWN"));

        mockMvc.perform(get("/api/students/by-std/{std}", "UNKNOWN")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/students doit créer un étudiant et retourner 201")
    void create_Success() throws Exception {
        when(studentService.create(any(), any(), any(), any(), any())).thenReturn(student);
        when(studentMapper.toResponse(student)).thenReturn(studentResponse);

        String requestBody =
                """
                {
                  "email": "henintsoa@gmail.com",
                  "password": "MotDePasse123!",
                  "firstName": "Henintsoa",
                  "lastName": "Maminiaina",
                  "stdNumber": "STD24020"
                }
                """;

        mockMvc
                .perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stdNumber").value("STD24020"));
    }

    @Test
    @DisplayName("POST /api/students doit retourner 409 si le matricule existe déjà")
    void create_Conflict() throws Exception {
        when(studentService.create(any(), any(), any(), any(), any()))
                .thenThrow(new ConflictException("Ce matricule est déjà utilisé : STD24020"));

        String requestBody =
                """
                {
                  "email": "henintsoa@gmail.com",
                  "password": "MotDePasse123!",
                  "firstName": "Henintsoa",
                  "lastName": "Maminiaina",
                  "stdNumber": "STD24020"
                }
                """;

        mockMvc
                .perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }
}