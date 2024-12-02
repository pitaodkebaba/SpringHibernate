package com.example.project.Backend.Responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlaylistResponse {
    private String name;
    private String owner;
    private List<String> songs;
}
