package com.example.test_task.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {

    @JsonProperty("first_name")
    @Min(2)
    @Max(100)
    @NotNull
    private String firstName;

    @JsonProperty("last_name")
    @Min(2)
    @Max(100)
    @NotNull
    private String lastName;

    @JsonProperty("middle_name")
    @Min(2)
    @Max(100)
    private String middleName;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @JsonProperty("email")
    @NotNull
    @Email
    private String email;

    @JsonProperty("phone")
    @NotNull
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Неверный формат номера телефона")
    private String phone;
}