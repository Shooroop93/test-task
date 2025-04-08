package com.example.test_task.controller;

import com.example.test_task.annotation.CustomControllerHandler;
import com.example.test_task.dto.request.user.UserContactsRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.exception.ExceptionValidatedRequestOrResponse;
import com.example.test_task.service.UserContactsService;
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
@RequestMapping("/v1/user/contacts")
@RequiredArgsConstructor
@CustomControllerHandler
@Slf4j
public class UserContactsController {

    private final UserContactsService userContactsService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApplicationResponse> create(@PathVariable Long userId, @Valid @RequestBody UserContactsRequest request, BindingResult bindingResult) throws ExceptionValidatedRequestOrResponse {
        if (bindingResult.hasErrors()) {
            throw new ExceptionValidatedRequestOrResponse(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userContactsService.createContact(userId, request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApplicationResponse> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userContactsService.getContactsByUserId(userId));
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<ApplicationResponse> updateContact(@PathVariable Long contactId, @Valid @RequestBody UserContactsRequest request, BindingResult bindingResult) throws ExceptionValidatedRequestOrResponse {
        if (bindingResult.hasErrors()) {
            throw new ExceptionValidatedRequestOrResponse(bindingResult);
        }
        return ResponseEntity.ok(userContactsService.updateContact(contactId, request));
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        userContactsService.deleteContact(contactId);
        return ResponseEntity.ok().build();
    }
}