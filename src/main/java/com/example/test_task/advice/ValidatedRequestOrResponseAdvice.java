package com.example.test_task.advice;

import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.dto.response.Errors;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ValidatedRequestOrResponseAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationResponse> handleValidateException(BindException exception) {
        BindingResult result = exception.getBindingResult();
        ApplicationResponse response = new ApplicationResponse();

        List<String> errorMessages = result.getFieldErrors().stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        Errors error = new Errors(HttpStatus.BAD_REQUEST.value(), errorMessages);
        response.setErrorList(error);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApplicationResponse> handleInvalidFormatException(InvalidFormatException exception) {
        ApplicationResponse response = new ApplicationResponse();
        List<String> errorMessages = exception.getPath().stream()
                .map(ref -> String.format("Поле '%s': некорректное значение '%s'",
                        ref.getFieldName(), exception.getValue()))
                .toList();

        Errors error = new Errors(HttpStatus.BAD_REQUEST.value(), errorMessages);
        response.setErrorList(error);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApplicationResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        String message = "Что-то пошло не так, не забудь проверить и обработать метод handleDataIntegrityViolation.";

        if (exception.getCause() != null && exception.getCause().getMessage() != null) {
            String causeMsg = exception.getCause().getMessage().toLowerCase();

            if (causeMsg.contains("duplicate key")) {
                if (causeMsg.contains("users_email_key")) {
                    message = "Пользователь с таким email уже существует";
                } else if (causeMsg.contains("users_phone_key")) {
                    message = "Пользователь с таким номером телефона уже существует";
                } else {
                    String keyName = extractConstraintName(causeMsg);
                    message = "Уникальное значение уже используется: " + keyName;
                }
            }
        }
        ApplicationResponse response = new ApplicationResponse();
        response.setErrorList(new Errors(HttpStatus.CONFLICT.value(), List.of(message)));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    private String extractConstraintName(String causeMsg) {
        String marker = "constraint \"";
        int start = causeMsg.indexOf(marker);
        if (start != -1) {
            int end = causeMsg.indexOf("\"", start + marker.length());
            if (end != -1) {
                return causeMsg.substring(start + marker.length(), end);
            }
        }
        return "неизвестное ограничение";
    }
}