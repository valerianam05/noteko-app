package com.example.demo.endpoint.rest.controller.health.Student;

import com.example.demo.entity.Student;
import com.example.demo.dto.StudentRegistrationForm;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "students/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new StudentRegistrationForm());
        return "students/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") StudentRegistrationForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "students/form";
        }
        Student student = studentService.create(
                form.getEmail(), form.getPassword(),
                form.getFirstName(), form.getLastName(), form.getStdNumber());
        return "redirect:/students/" + student.getUserId();
    }
}