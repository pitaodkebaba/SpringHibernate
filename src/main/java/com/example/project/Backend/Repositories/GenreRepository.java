package com.example.project.Backend.Repositories;

import com.example.project.Backend.Models.Genre;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    boolean existsByName(@NotBlank(message = "Name is mandatory") String name);
}
