package com.example.demo.endpoint.rest.controller.health.AcademicYear;


import com.example.demo.entity.AcademicYear;
import com.example.demo.service.AcademicYearService;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/academic-years")
@RequiredArgsConstructor
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("academicYears", academicYearService.findAll());
        return "academic-years/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("academicYear", new AcademicYear());
        return "academic-years/form";
    }

    @PostMapping
    public String create(@RequestParam LocalDate dateDebut,
                         @RequestParam LocalDate dateFin,
                         @RequestParam(defaultValue = "false") boolean isCurrent) {
        academicYearService.create(dateDebut, dateFin, isCurrent);
        return "redirect:/academic-years";
    }

    @PostMapping("/{id}/set-current")
    public String setCurrent(@PathVariable UUID id) {
        academicYearService.setAsCurrent(id);
        return "redirect:/academic-years";
    }
}