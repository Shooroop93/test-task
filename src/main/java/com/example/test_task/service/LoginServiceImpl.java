package com.example.test_task.service;

import com.example.test_task.constant.JWTokenType;
import com.example.test_task.dto.TokenObject;
import com.example.test_task.dto.request.auth.AuthRequest;
import com.example.test_task.dto.response.auth.AuthResponse;
import com.example.test_task.model.Users;
import com.example.test_task.service.interfaces.LoginService;
import com.example.test_task.service.interfaces.TokenService;
import com.example.test_task.service.interfaces.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final BCryptPasswordEncoder encoder;
    private final UsersService usersService;
    private final TokenService tokenService;

    @Override
    public ResponseEntity<?> auth(AuthRequest request, UUID sessionId) {

        Optional<Users> usersOptional = usersService.getUserByLogin(request.getLogin());

        if (usersOptional.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Users user = usersOptional.get();

        log.info("Check password");
        if (encoder.matches(request.getPassword(), user.getPassword())) {
            log.info("Password matches");
            TokenObject access = tokenService.generateToken(user, JWTokenType.ACCESS, sessionId);
            TokenObject refresh = tokenService.generateToken(user, JWTokenType.REFRESH, sessionId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new AuthResponse(access, refresh));
        } else {
            log.info("Password not matches");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}