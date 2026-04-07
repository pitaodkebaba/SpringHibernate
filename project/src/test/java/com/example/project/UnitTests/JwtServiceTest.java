package com.example.project.UnitTests;

import com.example.project.Backend.Models.User;
import com.example.project.Backend.Services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService.setSecretKey("7913c99e3e5d5b73e32fb11fa08bb29d91fcc5d284c55bc948232bb6405fa2f1");
        jwtService.setJwtExpiration(3600000);
    }


    @Test
    @DisplayName("Generate token")
    void shouldGenerateToken() {
        User user = new User();
        user.setUsername("testuser");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
    }

    @Test
    @DisplayName("Validate token")
    void shouldValidateToken() {
        User user = new User();
        user.setUsername("testuser");

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, user));
    }
}