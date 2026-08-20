package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.endpoint.rest.controller.grade.Auth.AuthController;
import com.example.demo.service.AuthService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock private AuthService authService;

  @InjectMocks private AuthController authController;

  private LoginRequest loginRequest;
  private LoginResponse loginResponse;

  @BeforeEach
  void setUp() {
    loginRequest = new LoginRequest("admin@exemple.com", "motdepasse123");
    loginResponse = new LoginResponse("fake-jwt-token", "ADMIN", UUID.randomUUID());
  }

  @Test
  @DisplayName("login doit retourner un token si les identifiants sont valides")
  void login_Success() {
    when(authService.login(loginRequest)).thenReturn(loginResponse);

    LoginResponse result = authController.login(loginRequest);

    assertNotNull(result);
    assertEquals(loginResponse, result);

    verify(authService).login(loginRequest);
  }
}
