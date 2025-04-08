package com.example.test_task.dto.response;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.response.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse {

    @JsonProperty("user_id")
    private Long userId;
    private List<UserResponse> users;
    @JsonProperty("message_status")
    private MessageStatus messageStatus;
    @JsonProperty("error_list")
    private Errors errorList;

    @JsonIgnore
    public void addUserToList(UserResponse userResponse) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(userResponse);
    }
}