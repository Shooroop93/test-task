package com.example.test_task.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Errors {

    @JsonProperty("status_code")
    private int statusCode;
    @JsonProperty("error_message")
    private List<String> errors;

    public Errors(int statusCode, String error) {
        this.statusCode = statusCode;
        if (errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
}