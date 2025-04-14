package com.example.test_task.service;

import com.example.test_task.constant.JWTokenType;
import com.example.test_task.dto.TokenObject;
import com.example.test_task.model.Tokens;
import com.example.test_task.model.Users;
import com.example.test_task.repository.TokensRepository;
import com.example.test_task.service.interfaces.TokenService;
import com.example.test_task.utils.JwtUtils;
import com.example.test_task.utils.TimeUtils;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtils jwtUtils;
    private final TokensRepository tokensRepository;

    @Value("${spring.application.name}")
    private String projectName;

    @Value("${spring.security.jwt.expiration.access}")
    private long expirationAccess;

    @Value("${spring.security.jwt.expiration.refresh}")
    private long expirationRefresh;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TokenObject generateToken(Users user, JWTokenType tokenType, UUID sessionId) {
        log.info("Start generating JWT token for user: {}", user.getLogin());

        Instant now = Instant.now();
        long lifeTime = switch (tokenType) {
            case ACCESS -> expirationAccess;
            case REFRESH -> expirationRefresh;
        };

        String idToken = UUID.randomUUID().toString();
        Date createDate = Date.from(now);
        Date expirationDate = Date.from(now.plusSeconds(lifeTime));

        String token = jwtUtils.generateToken(
                Jwts.builder()
                        .id(idToken)
                        .issuer(projectName)
                        .subject(user.getLogin())
                        .claim("token_type", tokenType.name())
                        .claim("session_id", sessionId)
                        .issuedAt(createDate)
                        .expiration(expirationDate)
        );

        Tokens tokenModel = new Tokens();
        tokenModel.setToken(token);
        tokenModel.setRevoked(false);
        tokenModel.setCreatedAt(TimeUtils.convertToLocalDateTime(createDate));
        tokenModel.setExpiresAt(TimeUtils.convertToLocalDateTime(expirationDate));
        tokenModel.setOwner(user);
        tokenModel.setTokenType(tokenType.name());
        tokenModel.setSessionId(sessionId);
        user.addToken(tokenModel);

        tokensRepository.save(tokenModel);

        log.info("Token successfully generated for type '{}'", tokenType);
        return new TokenObject(token, lifeTime);
    }
}