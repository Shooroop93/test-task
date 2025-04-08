package com.example.test_task.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}