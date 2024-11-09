package com.example.project.Controllers;

import com.example.project.Dtos.UserDto;
import com.example.project.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public Optional<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public void createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }
}
