package com.example.test_task.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPhotoResponse {

    @JsonProperty("id_photo")
    private Long id;

    @JsonProperty("url_photo")
    private String urlPhoto;
}