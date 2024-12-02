package com.example.project.Backend.Repositories;

import com.example.project.Backend.Models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Integer> {
    boolean existsByTitleAndAlbum(String title, String album);
}
