package com.example.test_task.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("middle_name")
    private String middleName;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;
}
