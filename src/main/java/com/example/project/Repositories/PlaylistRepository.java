package com.example.project.Repositories;

import com.example.project.Models.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    boolean existsByNameAndOwner(String name, String owner);
}
