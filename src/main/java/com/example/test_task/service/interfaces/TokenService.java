package com.example.test_task.service.interfaces;

import com.example.test_task.constant.JWTokenType;
import com.example.test_task.dto.TokenObject;
import com.example.test_task.model.Users;

import java.util.UUID;

public interface TokenService {

    TokenObject generateToken(Users user, JWTokenType tokenType, UUID sessionId);
}