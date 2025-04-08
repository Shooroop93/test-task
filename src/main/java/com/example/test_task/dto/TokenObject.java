package com.example.test_task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenObject {

    private String token;
    private long lifeTime;
}