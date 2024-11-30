package com.example.project.Responses;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetPlaylistResponse {
    private String name;
    private String username;
    private List<String> songs;
}
