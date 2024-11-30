package com.example.project.Services;


import com.example.project.Dtos.CreatePlaylistDto;
import com.example.project.Models.Playlist;
import com.example.project.Models.Song;
import com.example.project.Models.User;
import com.example.project.Repositories.PlaylistRepository;
import com.example.project.Repositories.SongRepository;
import com.example.project.Repositories.UserRepository;
import com.example.project.Responses.GetPlaylistResponse;
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

    public List<Playlist> getPlaylists() {
        return playlistRepository.findAll();
    }

    public Optional<GetPlaylistResponse> getPlaylistById(int id) {
        return playlistRepository.findById(id)
                .map(playlist -> {
                    GetPlaylistResponse response = new GetPlaylistResponse();
                    response.setName(playlist.getName());
                    response.setUsername(playlist.getUser().getUsername());
                    response.setSongs(playlist.getSongs().stream().map(Song::getTitle).collect(Collectors.toList()));
                    return response;
                });
    }

    @Transactional
    public void createPlaylist(int userId, CreatePlaylistDto playlist) throws AccessDeniedException {
        Playlist newPlaylist = convertToEntity(playlist);
        User user = userRepository.findById(userId).orElseThrow();
        newPlaylist.setUser(user);
        playlistRepository.save(newPlaylist);
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

    private CreatePlaylistDto convertToDto(Playlist playlist) {
        CreatePlaylistDto createPlaylistDto = new CreatePlaylistDto();
        createPlaylistDto.setName(playlist.getName());
        createPlaylistDto.setSongIds(playlist.getSongs().stream().map(Song::getId).collect(Collectors.toList()));
        return createPlaylistDto;
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
