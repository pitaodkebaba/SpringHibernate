package com.example.project.Backend.Services;

import com.example.project.Backend.Dtos.CreateSongDto;
import com.example.project.Backend.Models.Genre;
import com.example.project.Backend.Models.Song;
import com.example.project.Backend.Models.User;
import com.example.project.Backend.Repositories.GenreRepository;
import com.example.project.Backend.Repositories.SongRepository;
import com.example.project.Backend.Responses.AdminSongResponse;
import com.example.project.Backend.Responses.SongResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final GenreRepository genreRepository;

    public List<? extends SongResponse> getAllSongs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
            return songRepository.findAll().stream()
                    .map(this::convertToAdminResponse)
                    .collect(Collectors.toList());
        } else {
            return songRepository.findAll().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        }
    }

    public Optional<Song> getSongById(int id) {
        return songRepository.findById(id);
    }

    @Transactional
    public void createSong(CreateSongDto song) {
        if (songRepository.existsByTitleAndAlbum(song.getTitle(), song.getAlbum())) {
            throw new IllegalArgumentException("Song already exists on that album");
        }
        songRepository.save(convertToEntity(song));
    }

    @Transactional
    public void updateSong(int id, CreateSongDto song) {
        if (!songRepository.existsById(id)) {
            throw new IllegalArgumentException("Song with id: " + id + " not found");
        }
        Song songToUpdate = songRepository.findById(id).orElseThrow();
        songToUpdate.setTitle(song.getTitle());
        songToUpdate.setArtist(song.getArtist());
        songToUpdate.setAlbum(song.getAlbum());
        songToUpdate.setGenre(getOrCreateGenre(song.getGenreName()));
        songRepository.save(songToUpdate);
    }

    public void deleteSong(int id) {
        if (!songRepository.existsById(id)) {
            throw new IllegalArgumentException("Song with id: " + id + " not found");
        }
        songRepository.delete(songRepository.findById(id).orElseThrow());
    }

    private Genre getOrCreateGenre(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName(genreName);
                    return genreRepository.save(newGenre);
                });
    }

    public CreateSongDto convertToDto(Song song) {
        CreateSongDto createSongDto = new CreateSongDto();
        createSongDto.setTitle(song.getTitle());
        createSongDto.setArtist(song.getArtist());
        createSongDto.setAlbum(song.getAlbum());
        createSongDto.setGenreName(song.getGenre().getName());
        return createSongDto;
    }

    public Song convertToEntity(CreateSongDto createSongDto) {
        Song song = new Song();
        song.setTitle(createSongDto.getTitle());
        song.setArtist(createSongDto.getArtist());
        song.setAlbum(createSongDto.getAlbum());
        song.setGenre(getOrCreateGenre(createSongDto.getGenreName()));
        return song;
    }

    private SongResponse convertToResponse(Song song) {
        return SongResponse.builder()
                .id(String.valueOf(song.getId()))
                .title(song.getTitle())
                .artist(song.getArtist())
                .album(song.getAlbum())
                .genre(song.getGenre().getName())
                .build();
    }

    private AdminSongResponse convertToAdminResponse(Song song) {
        return AdminSongResponse.builder()
                .id(String.valueOf(song.getId()))
                .title(song.getTitle())
                .artist(song.getArtist())
                .album(song.getAlbum())
                .genre(song.getGenre().getName())
                .genreId(String.valueOf(song.getGenre().getId()))
                .build();
    }
}
