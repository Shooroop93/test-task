package com.example.test_task.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UserPhotoRequest {

    @NotNull(message = "user_id не может быть null")
    @JsonProperty("user_id")
    private Long userId;

    @NotBlank(message = "url_photo не может быть пустым")
    @URL
    @JsonProperty("url_photo")
    private String urlPhoto;
}