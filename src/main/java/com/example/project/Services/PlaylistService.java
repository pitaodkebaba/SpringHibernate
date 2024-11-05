package com.example.project.Services;


import com.example.project.Models.Playlist;
import com.example.project.Models.User;
import com.example.project.Repositories.PlaylistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaylistService {
    private PlaylistRepository playlistRepository;

    private List<Playlist> getPlaylistByName(String name) {
        return playlistRepository.findByName(name);
    }

    private List<Playlist> getPlaylistsByUser(Long userId) {
        return playlistRepository.findByUser(userId);
    }
}
