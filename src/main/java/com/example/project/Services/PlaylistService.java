package com.example.project.Services;


import com.example.project.Dtos.PlaylistDto;
import com.example.project.Models.Playlist;
import com.example.project.Models.Song;
import com.example.project.Models.User;
import com.example.project.Repositories.PlaylistRepository;
import com.example.project.Repositories.SongRepository;
import com.example.project.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlaylistService {
    public final PlaylistRepository playlistRepository;
    public final SongRepository songRepository;
    private final UserRepository userRepository;

    public List<PlaylistDto> getPlaylists() {
        return playlistRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PlaylistDto> getPlaylistById(int id) {
        return playlistRepository.findById(id)
                .map(this::convertToDto);
    }

    public void createPlaylist(Playlist playlist){
        playlistRepository.save(playlist);
    }

    public void addSongToPlaylist(int playlistId, int songId){
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
        Song song = songRepository.findById(songId).orElseThrow();
        playlist.getSongs().add(song);
        playlistRepository.save(playlist);
    }

    public void deletePlaylist(int id){
        playlistRepository.deleteById(id);
    }

    private PlaylistDto convertToDto(Playlist playlist) {
        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setId(playlist.getId());
        playlistDto.setName(playlist.getName());
        playlistDto.setOwner(playlist.getUser().getUsername());
        playlistDto.setSongs(playlist.getSongs().stream().map(Song::getTitle).collect(Collectors.toList()));
        return playlistDto;
    }

    private Playlist convertToEntity(PlaylistDto playlistDto){
        Playlist playlist = new Playlist();
        playlist.setName(playlistDto.getName());
        playlist.setId(playlistDto.getId());

        User user = userRepository.findByEmail(playlistDto.getOwner()).orElseThrow();
        playlist.setUser(user);

        List<Song> songs = playlistDto.getSongs().stream()
                .map(songRepository::findByTitle)
                .collect(Collectors.toList());
        playlist.setSongs(songs);

        return playlist;
    }
}
