package com.example.project.UnitTests;

import com.example.project.Backend.Dtos.CreateGenreDto;
import com.example.project.Backend.Models.Genre;
import com.example.project.Backend.Repositories.GenreRepository;
import com.example.project.Backend.Services.GenreService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("All genres retrieval")
    void shouldGetAllGenres() {
        genreService.getAllGenres();

        verify(genreRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Genre creation")
    void shouldCreateGenre() {
        CreateGenreDto createGenreDto = new CreateGenreDto();
        createGenreDto.setName("Test Genre");

        when(genreRepository.existsByName(anyString())).thenReturn(false);

        genreService.createGenre(createGenreDto);

        verify(genreRepository, times(1)).save(any(Genre.class));
    }

    @Test
    @DisplayName("Genre creation with existing name")
    void shouldNotCreateGenre() {
        CreateGenreDto createGenreDto = new CreateGenreDto();
        createGenreDto.setName("Test Genre");

        when(genreRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> genreService.createGenre(createGenreDto));
    }

    @Test
    @DisplayName("Genre deletion")
    void shouldDeleteGenre() {
        int genreId = 1;

        when(genreRepository.existsById(genreId)).thenReturn(true);

        genreService.deleteGenre(genreId);

        verify(genreRepository, times(1)).deleteById(genreId);
    }

    @Test
    @DisplayName("Genre deletion with non-existent genre")
    void shouldNotDeleteGenre() {
        int genreId = 1;

        when(genreRepository.existsById(genreId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> genreService.deleteGenre(genreId));
    }
}
