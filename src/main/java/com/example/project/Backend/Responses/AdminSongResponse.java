package com.example.project.Backend.Responses;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSongResponse extends SongResponse {
    private String id;
    private String genreId;
}
