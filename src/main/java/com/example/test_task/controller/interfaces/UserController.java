package com.example.test_task.controller.interfaces;

import com.example.test_task.annotation.CustomControllerHandler;
import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {

    @PostMapping
    @CustomControllerHandler
    ResponseEntity<ApplicationResponse> createUser(@Valid @RequestBody UserRequest request);

    @GetMapping
    ResponseEntity<ApplicationResponse> getALlUsers();

    @GetMapping("/{id}")
    @CustomControllerHandler
    ResponseEntity<ApplicationResponse> getById(@PathVariable Long id);

    @DeleteMapping("/{id}")
    ResponseEntity<?> removeById(@PathVariable Long id);

    @PutMapping("/{id}")
    @CustomControllerHandler
    ResponseEntity<ApplicationResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request);
}