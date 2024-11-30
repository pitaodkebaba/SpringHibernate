package com.example.project.Controllers;

import com.example.project.Dtos.UserDto;
import com.example.project.Models.User;
import com.example.project.Services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public Optional<UserDto> getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public void createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getAuthenticatedUser() throws AccessDeniedException {
        User authenticatedUser = userService.authencticatedUser();
        UserDto userDto = new UserDto();
        userDto.setId(authenticatedUser.getId());
        userDto.setUsername(authenticatedUser.getUsername());
        userDto.setEmail(authenticatedUser.getEmail());
        userDto.setPassword(authenticatedUser.getPassword());
        return ResponseEntity.ok(userDto);
    }
}
