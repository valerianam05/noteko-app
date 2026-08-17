package com.example.demo.endpoint.rest.controller.grade;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @GetMapping("/test-hello")
  public String ping() {
    return "pong";
  }
}
