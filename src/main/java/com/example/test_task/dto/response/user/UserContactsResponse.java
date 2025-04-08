package com.example.test_task.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserContactsResponse {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;
}