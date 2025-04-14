package com.example.test_task.service.interfaces;

import com.example.test_task.dto.request.auth.AuthRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface LoginService {

    ResponseEntity<?> auth(AuthRequest request, UUID sessionId);
}
