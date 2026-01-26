package com.example.project.Backend.Repositories;

import com.example.project.Backend.Models.Playlist;
import com.example.project.Backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    boolean existsByNameAndUser(String name, User user);
    List<Playlist> findAllByUser(User user);
}
