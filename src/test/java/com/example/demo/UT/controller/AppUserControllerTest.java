package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.endpoint.rest.controller.grade.AppUserController;
import com.example.demo.entity.AppUser;
import com.example.demo.entity.enums.UserRole;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.AppUserService;
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
class AppUserControllerTest {

  @Mock private AppUserService appUserService;

  @InjectMocks private AppUserController appUserController;

  private UUID userId;
  private AppUser appUser;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();

    appUser =
        AppUser.builder()
            .id(userId)
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .role(UserRole.ADMIN)
            .build();
  }

  @Test
  @DisplayName("list doit retourner tous les utilisateurs")
  void list_Success() {
    when(appUserService.findAll()).thenReturn(List.of(appUser));

    List<AppUser> result = appUserController.list();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(appUser, result.get(0));

    verify(appUserService).findAll();
  }

  @Test
  @DisplayName("detail doit retourner l'utilisateur demandé")
  void detail_Success() {
    when(appUserService.findById(userId)).thenReturn(appUser);

    AppUser result = appUserController.detail(userId);

    assertNotNull(result);
    assertEquals(appUser, result);

    verify(appUserService).findById(userId);
  }

  @Test
  @DisplayName("detail doit lever ResourceNotFoundException si l'utilisateur n'existe pas")
  void detail_NotFound() {
    when(appUserService.findById(userId))
        .thenThrow(new ResourceNotFoundException("Utilisateur introuvable : " + userId));

    assertThrows(ResourceNotFoundException.class, () -> appUserController.detail(userId));
  }

  @Test
  @DisplayName("delete doit supprimer l'utilisateur")
  void delete_Success() {
    doNothing().when(appUserService).delete(userId);

    assertDoesNotThrow(() -> appUserController.delete(userId));

    verify(appUserService).delete(userId);
  }

  @Test
  @DisplayName("delete doit lever ResourceNotFoundException si l'utilisateur n'existe pas")
  void delete_NotFound() {
    doThrow(new ResourceNotFoundException("Utilisateur introuvable : " + userId))
        .when(appUserService)
        .delete(userId);

    assertThrows(ResourceNotFoundException.class, () -> appUserController.delete(userId));
  }
}
