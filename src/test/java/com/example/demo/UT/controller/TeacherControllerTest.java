package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.TeacherCreateRequest;
import com.example.demo.dto.response.TeacherResponse;
import com.example.demo.endpoint.rest.controller.grade.TeacherController;
import com.example.demo.entity.Teacher;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.service.TeacherService;
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
class TeacherControllerTest {

    @Mock private TeacherService teacherService;

    @Mock private TeacherMapper teacherMapper;

    @InjectMocks private TeacherController teacherController;

    private UUID teacherId;
    private Teacher teacher;
    private TeacherResponse response;

    @BeforeEach
    void setUp() {
        teacherId = UUID.randomUUID();

        teacher = Teacher.builder().userId(teacherId).specialite("Informatique").build();

        // TeacherResponse est un record (comme StudentResponse, CourseResponse...) : les
        // records sont des classes `final`, donc Mockito ne peut pas les mocker avec
        // mock(...). On construit une vraie instance à la place.
        response = new TeacherResponse(teacherId, "Toky Ramarozaka", "toky@gmail.com", "Informatique");
    }

    @Test
    @DisplayName("list doit retourner tous les enseignants")
    void list_Success() {
        when(teacherService.findAll()).thenReturn(List.of(teacher));
        when(teacherMapper.toResponse(teacher)).thenReturn(response);

        List<TeacherResponse> result = teacherController.list();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));

        verify(teacherService).findAll();
        verify(teacherMapper).toResponse(teacher);
    }

    @Test
    @DisplayName("detail doit retourner un enseignant par son ID")
    void detail_Success() {
        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toResponse(teacher)).thenReturn(response);

        TeacherResponse result = teacherController.detail(teacherId);

        assertNotNull(result);
        assertEquals(response, result);

        verify(teacherService).findById(teacherId);
        verify(teacherMapper).toResponse(teacher);
    }

    @Test
    @DisplayName("create doit créer un enseignant et retourner sa réponse")
    void create_Success() {
        TeacherCreateRequest request =
                new TeacherCreateRequest("toky@gmail.com", "teacher123", "Toky", "Ramarozaka", "Informatique");

        when(teacherService.create(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.specialite()))
                .thenReturn(teacher);

        when(teacherMapper.toResponse(teacher)).thenReturn(response);

        TeacherResponse result = teacherController.create(request);

        assertNotNull(result);
        assertEquals(response, result);

        verify(teacherService)
                .create(
                        request.email(),
                        request.password(),
                        request.firstName(),
                        request.lastName(),
                        request.specialite());

        verify(teacherMapper).toResponse(teacher);
    }
}