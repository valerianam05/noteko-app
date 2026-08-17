package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.enums.Parcours;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CourseRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

  private final CourseRepository courseRepository;

  @Transactional(readOnly = true)
  public List<Course> findAll() {
    return courseRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Course> findByParcours(String parcours) {
    return courseRepository.findByParcours(Parcours.valueOf(parcours));
  }

  @Transactional(readOnly = true)
  public List<Course> findBySemester(UUID semesterId) {
    return courseRepository.findBySemesterId(semesterId);
  }

  @Transactional(readOnly = true)
  public List<Course> findByParcoursAndSemester(String parcours, UUID semesterId) {
    return courseRepository.findByParcoursAndSemesterId(parcours, semesterId);
  }

  @Transactional(readOnly = true)
  public Course findById(UUID id) {
    return courseRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable avec l'ID : " + id));
  }
}
