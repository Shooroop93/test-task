package com.example.test_task.controller;

import com.example.test_task.controller.interfaces.AuthController;
import com.example.test_task.dto.request.auth.AuthRequest;
import com.example.test_task.service.interfaces.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImpl implements AuthController {

    private final LoginService loginService;

    @Override
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        log.info("Received login request for user: {}", request.getLogin());
        return loginService.auth(request, UUID.randomUUID());
    }
}