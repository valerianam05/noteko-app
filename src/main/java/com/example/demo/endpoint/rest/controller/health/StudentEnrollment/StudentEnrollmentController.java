package com.example.demo.endpoint.rest.controller.health.StudentEnrollment;

import com.example.demo.dto.StudentEnrollmentForm;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.StudentEnrollmentService;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class StudentEnrollmentController {

    private final StudentEnrollmentService studentEnrollmentService;

    @GetMapping
    public String list(@RequestParam UUID studentId, Model model) {
        model.addAttribute("enrollments", studentEnrollmentService.findByStudent(studentId));
        return "enrollments/list";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam UUID studentId, Model model) {
        StudentEnrollmentForm form = new StudentEnrollmentForm();
        form.setStudentId(studentId);
        model.addAttribute("form", form);
        return "enrollments/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") StudentEnrollmentForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "enrollments/form";
        }
        studentEnrollmentService.enroll(
                form.getStudentId(), form.getGroupId(),
                form.getSemesterId(), form.getAcademicYearId());
        return "redirect:/enrollments?studentId=" + form.getStudentId();
    }
}