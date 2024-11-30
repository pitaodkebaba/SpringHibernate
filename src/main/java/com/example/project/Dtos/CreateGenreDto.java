package com.example.project.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateGenreDto {
    @NotBlank(message = "Name is mandatory")
    private String name;
}
