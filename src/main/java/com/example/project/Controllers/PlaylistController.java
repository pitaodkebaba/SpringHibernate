package com.example.project.Controllers;

import com.example.project.Models.Playlist;
import com.example.project.Repositories.PlaylistRepository;
import com.example.project.Services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
}
