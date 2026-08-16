package com.example.demo.endpoint.rest.controller.health.AppUser;


import com.example.demo.service.AppUserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", appUserService.findAll());
        return "users/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        model.addAttribute("user", appUserService.findById(id));
        return "users/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id) {
        appUserService.delete(id);
        return "redirect:/users";
    }
}