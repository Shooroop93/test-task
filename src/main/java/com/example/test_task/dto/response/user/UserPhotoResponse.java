package com.example.test_task.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserPhotoResponse {

    @JsonProperty("id_photo")
    private Long id;

    @JsonProperty("url_photo")
    private String urlPhoto;
}