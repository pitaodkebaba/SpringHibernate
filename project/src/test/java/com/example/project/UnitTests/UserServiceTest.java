package com.example.project.UnitTests;

import com.example.project.Backend.Dtos.UserDto;
import com.example.project.Backend.Models.User;
import com.example.project.Backend.Repositories.UserRepository;
import com.example.project.Backend.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("All users retrieval")
    void shouldGetAllUsers() {
        userService.getUsers();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("User retrieval by id")
    void shouldGetUserById() {
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(new User()));

        userService.getUserById(userId);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("User creation")
    void shouldCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("Test User");
        userDto.setPassword("password");
        userDto.setEmail("test@test.pl");
        userDto.setId(1);

        when(userRepository.existsById(anyInt())).thenReturn(false);

        userService.createUser(userDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("User creation with existing user")
    void shouldNotCreateUserWithExistingId() {
        UserDto userDto = new UserDto();
        userDto.setUsername("Test User");
        userDto.setPassword("password");
        userDto.setEmail("test@test.pl");
        userDto.setId(1);

        when(userRepository.existsById(anyInt())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDto));
    }

    @Test
    @DisplayName("User deletion")
    void shouldDeleteUser() {
        int userId = 1;

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("User deletion with non-existent user")
    void shouldNotDeleteNonExistentUser() {
        int userId = 1;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(userId));
    }

    @Test
    @DisplayName("Authenticated user retrieval")
    void shouldGetAuthenticatedUser() throws AccessDeniedException {
        User user = new User();
        user.setId(1);
        user.setUsername("Test User");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        userService.authencticatedUser();

        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    @DisplayName("Authenticated user retrieval with no user")
    void shouldNotGetAuthenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(null);

        assertThrows(AccessDeniedException.class, () -> userService.authencticatedUser());
    }
}
