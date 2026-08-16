package com.example.demo.service;

import com.example.demo.entity.Semester;
import com.example.demo.entity.enums.SemesterCode;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SemesterRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SemesterService {

  private final SemesterRepository semesterRepository;

  public List<Semester> findAllOrdered() {
    return semesterRepository.findAllByOrderByOrderNumAsc();
  }

  public Semester findById(UUID id) {
    return semesterRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Semestre introuvable : " + id));
  }

  public Semester findByCode(SemesterCode code) {
    return semesterRepository
        .findByCode(code)
        .orElseThrow(() -> new ResourceNotFoundException("Semestre introuvable : " + code));
  }
}
