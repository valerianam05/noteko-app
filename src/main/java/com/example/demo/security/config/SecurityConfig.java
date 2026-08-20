package com.example.demo.security.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.example.demo.security.exception.RestAccessDeniedHandler;
import com.example.demo.security.exception.RestAuthenticationEntryPoint;
import com.example.demo.security.filter.BearerAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final RestAuthenticationEntryPoint entryPoint;
  private final RestAccessDeniedHandler accessDeniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, BearerAuthFilter bearerAuthFilter)
      throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
        .exceptionHandling(
            e -> e.authenticationEntryPoint(entryPoint).accessDeniedHandler(accessDeniedHandler))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/auth/login", "/ping", "/health/**")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.POST,
                        "/api/students",
                        "/api/teachers",
                        "/api/groups",
                        "/api/promotions",
                        "/api/courses",
                        "/api/academic-years")
                    .hasRole("ADMIN")
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .requestMatchers("/ui/**", "/css/**", "/js/**")
                    .permitAll()
                    .requestMatchers("/api/users", "/api/users/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/evaluations/**", "/api/grades/**")
                    .hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                    .requestMatchers("/api/evaluations/**", "/api/grades/**")
                    .hasAnyRole("TEACHER", "ADMIN")
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(bearerAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
