package com.example.project.Repositories;

import com.example.project.Models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByTitle(String title);
    List<Song> findByArtist(String artist);
    List<Song> findByAlbum(String album);
    List<Song> findByGenre(String genre);
}
