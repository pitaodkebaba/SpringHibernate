package com.example.project.Backend.Services;

import com.example.project.Backend.Dtos.CreateGenreDto;
import com.example.project.Backend.Models.Genre;
import com.example.project.Backend.Repositories.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    public GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Transactional
    public void createGenre(CreateGenreDto genre) {
        if (genreRepository.existsByName(genre.getName())) {
            throw new IllegalArgumentException("Genre already exists");
        }
        genreRepository.save(Genre.builder().name(genre.getName()).build());
    }

    public void deleteGenre(int id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre with id: " + id + " not found");
        }
        genreRepository.deleteById(id);
    }
}
