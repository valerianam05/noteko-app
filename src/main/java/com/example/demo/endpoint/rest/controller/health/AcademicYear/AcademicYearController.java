package com.example.demo.endpoint.rest.controller.health.AcademicYear;

import com.example.demo.dto.response.AcademicYearResponse;
import com.example.demo.mapper.AcademicYearMapper;
import com.example.demo.service.AcademicYearService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/academic-years")
@RequiredArgsConstructor
public class AcademicYearController {

  private final AcademicYearService academicYearService;
  private final AcademicYearMapper academicYearMapper;

  @GetMapping
  public List<AcademicYearResponse> list() {
    return academicYearService.findAll().stream().map(academicYearMapper::toResponse).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AcademicYearResponse create(
      @RequestParam LocalDate dateDebut,
      @RequestParam LocalDate dateFin,
      @RequestParam(defaultValue = "false") boolean isCurrent) {
    return academicYearMapper.toResponse(academicYearService.create(dateDebut, dateFin, isCurrent));
  }

  @PostMapping("/{id}/set-current")
  public AcademicYearResponse setCurrent(@PathVariable UUID id) {
    return academicYearMapper.toResponse(academicYearService.setAsCurrent(id));
  }
}
