package com.example.project.Backend.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SongResponse {
    private String title;
    private String artist;
    private String album;
    private String genre;
}
