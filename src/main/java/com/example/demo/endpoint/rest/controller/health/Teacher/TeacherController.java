package com.example.demo.endpoint.rest.controller.health.Teacher;


import com.example.demo.entity.Teacher;
import com.example.demo.dto.TeacherRegistrationForm;
import com.example.demo.service.TeacherService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teachers/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        model.addAttribute("teacher", teacherService.findById(id));
        return "teachers/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new TeacherRegistrationForm());
        return "teachers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") TeacherRegistrationForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teachers/form";
        }
        Teacher teacher = teacherService.create(
                form.getEmail(), form.getPassword(), form.getFirstName(),
                form.getLastName(), form.getSpecialite());
        return "redirect:/teachers/" + teacher.getUserId();
    }
}