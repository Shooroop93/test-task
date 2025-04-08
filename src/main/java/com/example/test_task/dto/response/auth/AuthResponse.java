package com.example.test_task.dto.response.auth;

import com.example.test_task.dto.TokenObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private TokenObject accessToken;
    private TokenObject refreshToken;
}