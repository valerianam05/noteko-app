package com.example.demo.endpoint.rest.controller.health.AcademicGroup;

import com.example.demo.entity.AcademicGroup;
import com.example.demo.dto.AcademicGroupForm;
import com.example.demo.service.AcademicGroupService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class AcademicGroupController {

    private final AcademicGroupService academicGroupService;

    @GetMapping
    public String list(@RequestParam UUID promotionId, Model model) {
        model.addAttribute("groups", academicGroupService.findByPromotion(promotionId));
        model.addAttribute("promotionId", promotionId);
        return "groups/list";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam UUID promotionId, Model model) {
        AcademicGroupForm form = new AcademicGroupForm();
        form.setPromotionId(promotionId);
        model.addAttribute("form", form);
        return "groups/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") AcademicGroupForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "groups/form";
        }
        AcademicGroup group = academicGroupService.create(
                form.getName(), form.getParcours(), form.getPromotionId());
        return "redirect:/groups?promotionId=" + form.getPromotionId();
    }
}