package com.example.demo.endpoint.rest.controller.grade;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

  @GetMapping("/ui/login")
  public String loginPage() {
    return "login";
  }
}
