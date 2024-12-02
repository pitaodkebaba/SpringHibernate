package com.example.project.Backend.Controllers;

import com.example.project.Backend.Dtos.UserDto;
import com.example.project.Backend.Models.User;
import com.example.project.Backend.Responses.SuccessResponse;
import com.example.project.Backend.Services.UserService;
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
    public ResponseEntity<SuccessResponse<List<UserDto>>> getUsers() {
        List<UserDto> users = userService.getUsers();
        SuccessResponse<List<UserDto>> response = new SuccessResponse<>("Success", "Users retrieved successfully", users);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Optional<UserDto>>> getUserById(@PathVariable int id) {
        Optional<UserDto> user = userService.getUserById(id);
        SuccessResponse<Optional<UserDto>> response = new SuccessResponse<>("Success", "User with id: " + id + " retrieved successfully", user);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "User created successfully", null);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        SuccessResponse<Void> response = new SuccessResponse<>("Success", "User with id: " + id + " deleted successfully", null);
        return ResponseEntity.ok(response);
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
