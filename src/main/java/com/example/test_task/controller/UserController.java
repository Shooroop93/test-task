package com.example.test_task.controller;

import com.example.test_task.annotation.CustomControllerHandler;
import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.exception.ExceptionValidatedRequestOrResponse;
import com.example.test_task.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@CustomControllerHandler
@Slf4j
public class UserController {

    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> createUser(@Valid @RequestBody UserRequest request, BindingResult bindingResult) throws ExceptionValidatedRequestOrResponse {
        if (bindingResult.hasErrors()) {
            throw new ExceptionValidatedRequestOrResponse(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<ApplicationResponse> getALlUsers() {
        ApplicationResponse response = usersService.getAllUsers();
        if (response.getErrorList() != null) {
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(@PathVariable Long id) {
        ApplicationResponse response = usersService.getUserById(id);
        if (response.getErrorList() != null) {
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeById(@PathVariable Long id) {
        usersService.removeUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request, BindingResult bindingResult) throws ExceptionValidatedRequestOrResponse {
        if (bindingResult.hasErrors()) {
            throw new ExceptionValidatedRequestOrResponse(bindingResult);
        }
        return ResponseEntity.ok(usersService.updateUser(id, request));
    }
}