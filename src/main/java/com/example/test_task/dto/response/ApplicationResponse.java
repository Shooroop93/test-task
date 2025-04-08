package com.example.test_task.dto.response;

import com.example.test_task.controller.MessageStatus;
import com.example.test_task.dto.request.user.UserRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApplicationResponse {

    private UserRequest user;
    private List<UserRequest> users;
    @JsonProperty("message_status")
    private MessageStatus messageStatus;
    @JsonProperty("error_list")
    private List<Error> errorList;

    public void addErrorList(Error error) {
        if (this.errorList == null) {
            this.errorList = new ArrayList<>();
        }
        this.errorList.add(error);
    }
}