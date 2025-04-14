package com.example.test_task.controller;

import com.example.test_task.controller.interfaces.UserController;
import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.service.interfaces.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {

    private final UsersService usersService;

    @Override
    public ResponseEntity<ApplicationResponse> createUser(@Valid @RequestBody UserRequest request) {
        return usersService.createUser(request);
    }

    @Override
    public ResponseEntity<ApplicationResponse> getALlUsers() {
        return usersService.getAllUsers();
    }

    @Override
    public ResponseEntity<ApplicationResponse> getById(@PathVariable Long id) {
        return usersService.getUserById(id);
    }

    @Override
    public ResponseEntity<?> removeById(@PathVariable Long id) {
        usersService.removeUser(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApplicationResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return usersService.updateUser(id, request);
    }
}