package com.example.test_task.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ExceptionValidatedRequestOrResponse extends RuntimeException {

    private final BindingResult bindingResult;

    public ExceptionValidatedRequestOrResponse(BindingResult bindingResult) {
        super("Validation failed");
        this.bindingResult = bindingResult;
    }
}