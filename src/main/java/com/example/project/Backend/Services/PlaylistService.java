package com.example.project.Backend.Services;


import com.example.project.Backend.Dtos.CreatePlaylistDto;
import com.example.project.Backend.Models.Playlist;
import com.example.project.Backend.Models.Song;
import com.example.project.Backend.Models.User;
import com.example.project.Backend.Repositories.PlaylistRepository;
import com.example.project.Backend.Repositories.SongRepository;
import com.example.project.Backend.Repositories.UserRepository;
import com.example.project.Backend.Responses.GetPlaylistResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlaylistService {
    public final PlaylistRepository playlistRepository;
    public final SongRepository songRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<GetPlaylistResponse> getPlaylists() {
        return playlistRepository.findAll().stream()
                .map(playlist -> {
                    GetPlaylistResponse response = new GetPlaylistResponse();
                    response.setName(playlist.getName());
                    response.setOwner(playlist.getUser().getUsername());
                    response.setSongs(playlist.getSongs().stream().map(Song::getTitle).collect(Collectors.toList()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<Playlist> getPlaylistsByCurrentUser() {
        try {
            User user = userService.authencticatedUser();
            return playlistRepository.findAllByUser(user);
        } catch (Exception e) {
            throw new EntityNotFoundException("You do not have any playlists");
        }
    }

    public Optional<Playlist> getPlaylistById(int id) throws AccessDeniedException {
        try {
            User user = userService.authencticatedUser();
            if (playlistRepository.findById(id).get().getUser().getId() != user.getId()) {
                throw new AccessDeniedException("This playlist does not belong to you");
            }
            return playlistRepository.findById(id);
        } catch (AccessDeniedException e) {
            throw new AccessDeniedException(e.getMessage());
        } catch (Exception e) {
            throw new EntityNotFoundException("Playlist with id: " + id + " not found");
        }
    }

    @Transactional
    public void createPlaylist(int userId, CreatePlaylistDto playlist){
        Playlist newPlaylist = convertToEntity(playlist);
        User user = userRepository.findById(userId).orElseThrow();
        if (playlistRepository.existsByNameAndUser(newPlaylist.getName(), user)) {
            throw new IllegalArgumentException("You already have a playlist with this name");
        }
        newPlaylist.setUser(user);
        playlistRepository.save(newPlaylist);
    }

    public void addSongToPlaylist(int playlistId, int songId){
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
        Song song = songRepository.findById(songId).orElseThrow();
        if (!playlistRepository.existsById(playlistId) && !songRepository.existsById(songId)) {
            throw new EntityNotFoundException("Playlist or Song not found");
        }
        playlist.getSongs().add(song);
        playlistRepository.save(playlist);
    }

    public void deletePlaylist(int id) throws AccessDeniedException {
        try {
            User user = userService.authencticatedUser();
            if (playlistRepository.findById(id).get().getUser().getId() != user.getId()) {
                throw new AccessDeniedException("This playlist does not belong to you");
            }
            playlistRepository.deleteById(id);
        } catch (AccessDeniedException e) {
            throw new AccessDeniedException(e.getMessage());
        } catch (Exception e) {
            throw new EntityNotFoundException("Playlist with id: " + id + " not found");
        }
    }

    private Playlist convertToEntity(CreatePlaylistDto createPlaylistDto){
        Playlist playlist = new Playlist();
        playlist.setName(createPlaylistDto.getName());

        List<Song> songs = createPlaylistDto.getSongIds().stream()
                .map(songRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        playlist.setSongs(songs);

        return playlist;
    }
}
