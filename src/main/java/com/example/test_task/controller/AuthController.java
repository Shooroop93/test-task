package com.example.test_task.controller;

import com.example.test_task.dto.request.auth.AuthRequest;
import com.example.test_task.exception.ExceptionValidatedRequestOrResponse;
import com.example.test_task.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request, BindingResult bindingResult) throws ExceptionValidatedRequestOrResponse {
        log.info("Received login request for user: {}", request.getLogin());
        if (bindingResult.hasErrors()) {
            throw new ExceptionValidatedRequestOrResponse(bindingResult);
        }
        return loginService.auth(request, UUID.randomUUID());
    }
}