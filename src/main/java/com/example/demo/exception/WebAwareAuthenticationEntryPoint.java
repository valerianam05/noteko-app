package com.example.demo.exception;

import com.example.demo.security.exception.RestAuthenticationEntryPoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebAwareAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    if (request.getRequestURI().startsWith("/ui/")) {
      response.sendRedirect("/ui/login");
      return;
    }

    restAuthenticationEntryPoint.commence(request, response, authException);
  }
}
