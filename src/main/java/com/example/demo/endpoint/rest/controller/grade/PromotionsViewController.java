package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PromotionsViewController {

  private final PromotionService promotionService;

  @GetMapping("/ui/promotions")
  public String listPromotions(Model model) {
    model.addAttribute("promotions", promotionService.findAll());
    return "promotions";
  }
}
