package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionForm {

    @NotBlank(message = "Le nom de la promotion est obligatoire")
    private String name;

    @NotNull(message = "L'année est obligatoire")
    @Min(value = 2000, message = "Année invalide")
    private Integer year;
}