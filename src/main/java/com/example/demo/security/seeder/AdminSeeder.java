package com.example.demo.security.seeder;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

  private final AppUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${admin.seed.email:}")
  private String adminEmail;

  @Value("${admin.seed.password:}")
  private String adminPassword;

  @Override
  public void run(String... args) {
    if (adminEmail.isBlank() || adminPassword.isBlank()) {
      log.info(
          "Variables ADMIN_SEED_EMAIL ou ADMIN_SEED_PASSWORD non définies. Initialisation de"
              + " l'admin ignorée.");
      return;
    }

    if (userRepository.findByEmail(adminEmail).isEmpty()) {
      AppUser admin = new AppUser();
      admin.setEmail(adminEmail);
      admin.setPassword(passwordEncoder.encode(adminPassword));
      admin.setRole(UserRole.ADMIN);

      userRepository.save(admin);
      log.info(">>> Compte administrateur initial créé avec succès : {}", adminEmail);
    } else {
      log.info("Compte administrateur {} existe déjà. Initialisation ignorée.", adminEmail);
    }
  }
}
