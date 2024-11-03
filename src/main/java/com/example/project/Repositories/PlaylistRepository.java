package com.example.project.Repositories;

import com.example.project.Models.Playlist;
import com.example.project.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser(User user);
    List<Playlist> findByName(String name);
}
