package com.example.test_task.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Error {

    @JsonProperty("list_error")
    private List<String> listError;
}