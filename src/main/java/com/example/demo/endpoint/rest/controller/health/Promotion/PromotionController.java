package com.example.demo.endpoint.rest.controller.health.Promotion;

import com.example.demo.entity.Promotion;
import com.example.demo.service.PromotionService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("promotions", promotionService.findAll());
        return "promotions/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "promotions/form";
    }

    @PostMapping
    public String create(@RequestParam String name, @RequestParam Integer year) {
        Promotion promotion = promotionService.create(name, year);
        return "redirect:/promotions/" + promotion.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        model.addAttribute("promotion", promotionService.findById(id));
        return "promotions/detail";
    }
}