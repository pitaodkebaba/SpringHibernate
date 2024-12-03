package com.example.project.UnitTests;

import com.example.project.Backend.Dtos.LoginUserDto;
import com.example.project.Backend.Dtos.RegisterUserDto;
import com.example.project.Backend.Models.User;
import com.example.project.Backend.Repositories.UserRepository;
import com.example.project.Backend.Services.AuthenticationService;
import com.example.project.Backend.Services.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("User signup")
    void shouldSignupUser() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("testuser");
        registerUserDto.setEmail("testuser@example.com");
        registerUserDto.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        authenticationService.signup(registerUserDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("User authentication")
    void shouldAuthenticateUser() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("testuser");
        loginUserDto.setPassword("password");

        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(new User()));

        authenticationService.authenticate(loginUserDto);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("User authentication with invalid credentials")
    void shouldNotAuthenticateUserWithInvalidCredentials() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("testuser");
        loginUserDto.setPassword("wrongpassword");

        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(loginUserDto));
    }
}
