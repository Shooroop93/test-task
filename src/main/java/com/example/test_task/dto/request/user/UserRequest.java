package com.example.test_task.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("first_name")
    @Size(min = 2, max = 100)
    @NotBlank
    @NotNull
    private String firstName;

    @JsonProperty("last_name")
    @Size(min = 2, max = 100)
    @NotBlank
    @NotNull
    private String lastName;

    @JsonProperty("middle_name")
    @Size(min = 2, max = 100)
    @NotBlank
    private String middleName;

    @JsonProperty("birth_date")
    private LocalDate birthDate;
}