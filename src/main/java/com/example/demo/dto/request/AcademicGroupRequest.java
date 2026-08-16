package com.example.demo.dto.request;

import com.example.demo.entity.enums.Parcours;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicGroupRequest {

    @NotBlank(message = "Le nom du groupe est obligatoire")
    private String name;

    @NotNull(message = "Le parcours est obligatoire")
    private Parcours parcours;

    @NotNull(message = "La promotion est obligatoire")
    private UUID promotionId;
}