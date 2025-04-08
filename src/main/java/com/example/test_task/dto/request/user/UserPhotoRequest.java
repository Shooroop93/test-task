package com.example.test_task.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UserPhotoRequest {

    @NotBlank
    @URL
    @JsonProperty("url_photo")
    private String urlPhoto;
}