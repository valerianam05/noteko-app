package com.example.demo.endpoint.rest.controller.health.AppUser;

import com.example.demo.entity.AppUser;
import com.example.demo.service.AppUserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {

  private final AppUserService appUserService;

  @GetMapping
  public List<AppUser> list() {
    return appUserService.findAll();
  }

  @GetMapping("/{id}")
  public AppUser detail(@PathVariable UUID id) {
    return appUserService.findById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    appUserService.delete(id);
  }
}
