package com.example.project.Backend.Responses;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class PlaylistResponse {
    private String name;
    private String owner;
    private List<String> songs;
}
