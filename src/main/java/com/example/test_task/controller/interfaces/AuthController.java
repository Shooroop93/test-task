package com.example.test_task.controller.interfaces;

import com.example.test_task.annotation.CustomControllerHandler;
import com.example.test_task.dto.request.auth.AuthRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    @PostMapping("/login")
    @CustomControllerHandler
    ResponseEntity<?> login(@Valid @RequestBody AuthRequest request);
}