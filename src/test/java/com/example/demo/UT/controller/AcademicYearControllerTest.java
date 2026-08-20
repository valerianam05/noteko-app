package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.response.AcademicYearResponse;
import com.example.demo.endpoint.rest.controller.grade.AcademicYearController;
import com.example.demo.entity.AcademicYear;
import com.example.demo.mapper.AcademicYearMapper;
import com.example.demo.service.AcademicYearService;
import java.time.LocalDate;
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
class AcademicYearControllerTest {

    @Mock private AcademicYearService academicYearService;

    @Mock private AcademicYearMapper academicYearMapper;

    @InjectMocks private AcademicYearController academicYearController;

    private UUID yearId;
    private AcademicYear academicYear;
    private AcademicYearResponse response;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @BeforeEach
    void setUp() {
        yearId = UUID.randomUUID();
        dateDebut = LocalDate.of(2026, 9, 1);
        dateFin = LocalDate.of(2027, 6, 30);

        academicYear =
                AcademicYear.builder()
                        .id(yearId)
                        .dateDebut(dateDebut)
                        .dateFin(dateFin)
                        .isCurrent(true)
                        .build();

        // AcademicYearResponse est probablement un record (comme les autres Response de ce
        // projet) : les records sont `final`, donc impossible à mocker avec mock(...). On
        // construit une vraie instance à la place.
        response = new AcademicYearResponse(yearId, "2026-2027", true);
    }

    @Test
    @DisplayName("list doit retourner toutes les années universitaires")
    void list_Success() {
        when(academicYearService.findAll()).thenReturn(List.of(academicYear));
        when(academicYearMapper.toResponse(academicYear)).thenReturn(response);

        List<AcademicYearResponse> result = academicYearController.list();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));

        verify(academicYearService).findAll();
    }

    @Test
    @DisplayName("create doit créer une année universitaire")
    void create_Success() {
        when(academicYearService.create(dateDebut, dateFin, true)).thenReturn(academicYear);
        when(academicYearMapper.toResponse(academicYear)).thenReturn(response);

        AcademicYearResponse result = academicYearController.create(dateDebut, dateFin, true);

        assertNotNull(result);
        assertEquals(response, result);

        verify(academicYearService).create(dateDebut, dateFin, true);
    }

    @Test
    @DisplayName("setCurrent doit définir l'année comme courante")
    void setCurrent_Success() {
        when(academicYearService.setAsCurrent(yearId)).thenReturn(academicYear);
        when(academicYearMapper.toResponse(academicYear)).thenReturn(response);

        AcademicYearResponse result = academicYearController.setCurrent(yearId);

        assertNotNull(result);
        assertEquals(response, result);

        verify(academicYearService).setAsCurrent(yearId);
    }
}