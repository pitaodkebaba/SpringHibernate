package com.example.project.Backend.Responses;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class LoginResponse {
    private String token;
    private Long expiresIn;

}
