package com.example.demo.service;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.Student;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.StudentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

  private final StudentRepository studentRepository;
  private final AppUserService appUserService;

  public List<Student> findAll() {
    return studentRepository.findAll();
  }

  public Student findById(UUID id) {
    return studentRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Étudiant introuvable : " + id));
  }

  public Student findByStdNumber(String stdNumber) {
    return studentRepository
        .findByStdNumber(stdNumber)
        .orElseThrow(
            () -> new ResourceNotFoundException("Étudiant introuvable, matricule : " + stdNumber));
  }

  public Student create(
      String email, String rawPassword, String firstName, String lastName, String stdNumber) {
    if (studentRepository.existsByStdNumber(stdNumber)) {
      throw new ConflictException("Ce matricule est déjà utilisé : " + stdNumber);
    }
    AppUser appUser =
        appUserService.create(email, rawPassword, firstName, lastName, UserRole.STUDENT);
    Student student = Student.builder().appUser(appUser).stdNumber(stdNumber).build();
    return studentRepository.save(student);
  }
}
