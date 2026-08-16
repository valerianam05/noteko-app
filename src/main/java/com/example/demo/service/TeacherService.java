package com.example.demo.service;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.Teacher;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TeacherRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {

  private final TeacherRepository teacherRepository;
  private final AppUserService appUserService;

  public List<Teacher> findAll() {
    return teacherRepository.findAll();
  }

  public Teacher findById(UUID id) {
    return teacherRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Enseignant introuvable : " + id));
  }

  public Teacher create(
      String email, String rawPassword, String firstName, String lastName, String specialite) {
    AppUser appUser =
        appUserService.create(email, rawPassword, firstName, lastName, UserRole.TEACHER);
    Teacher teacher = Teacher.builder().appUser(appUser).specialite(specialite).build();
    return teacherRepository.save(teacher);
  }
}
