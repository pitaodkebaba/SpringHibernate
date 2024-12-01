package com.example.project.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGenreDto {
    @NotBlank(message = "Genre name is mandatory")
    private String name;
}
