package com.example.test_task.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserContactsRequest {

    @JsonProperty("email")
    @NotNull
    @Email
    private String email;

    @JsonProperty("phone")
    @NotNull
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Неверный формат номера телефона")
    private String phone;
}