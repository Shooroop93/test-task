package com.example.test_task.controller.interfaces;

import com.example.test_task.annotation.CustomControllerHandler;
import com.example.test_task.dto.request.user.UserContactsRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserContactsController {

    @PostMapping("/{userId}")
    @CustomControllerHandler
    ResponseEntity<ApplicationResponse> create(@PathVariable Long userId, @Valid @RequestBody UserContactsRequest request);

    @GetMapping("/{userId}")
    ResponseEntity<ApplicationResponse> getByUserId(@PathVariable Long userId);

    @PutMapping("/{contactId}")
    @CustomControllerHandler
    ResponseEntity<ApplicationResponse> updateContact(@PathVariable Long contactId, @Valid @RequestBody UserContactsRequest request);

    @DeleteMapping("/{contactId}")
    ResponseEntity<Void> deleteContact(@PathVariable Long contactId);
}