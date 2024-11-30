package com.example.project.Controllers;

import com.example.project.Dtos.CreateGenreDto;
import com.example.project.Models.Genre;
import com.example.project.Responses.SuccessResponse;
import com.example.project.Services.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<Genre> getGenres() {
        return genreService.getAllGenres();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createGenre(CreateGenreDto genre) {
        genreService.createGenre(genre);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "Genre created successfully", null);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete/{id}")
    public void deleteGenre(@PathVariable String id) {
        genreService.deleteGenre(Integer.parseInt(id));
    }
}
