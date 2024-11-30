package com.example.project.Dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreatePlaylistDto {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private List<Integer> songIds;
}
