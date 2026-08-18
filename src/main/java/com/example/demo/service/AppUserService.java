package com.example.demo.service;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AppUserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {
  private final PasswordEncoder passwordEncoder;
  private final AppUserRepository appUserRepository;

  public List<AppUser> findAll() {
    return appUserRepository.findAll();
  }

  public AppUser findById(UUID id) {
    return appUserRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + id));
  }

  public AppUser create(
      String email, String rawPassword, String firstName, String lastName, UserRole role) {
    if (appUserRepository.existsByEmail(email)) {
      throw new ConflictException("Un compte existe déjà avec l'email : " + email);
    }
    AppUser user =
        AppUser.builder()
            .email(email)
            .password(
                rawPassword) // TODO: encoder avec PasswordEncoder une fois Spring Security en place
            .firstName(firstName)
            .lastName(lastName)
            .role(role)
            .build();
    return appUserRepository.save(user);
  }

  public void delete(UUID id) {
    if (!appUserRepository.existsById(id)) {
      throw new ResourceNotFoundException("Utilisateur introuvable : " + id);
    }
    appUserRepository.deleteById(id);
  }
}
