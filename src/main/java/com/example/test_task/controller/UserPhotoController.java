package com.example.test_task.controller;

import com.example.test_task.annotation.CustomControllerHandler;
import com.example.test_task.dto.request.user.UserPhotoRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.exception.ExceptionValidatedRequestOrResponse;
import com.example.test_task.service.UserPhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/photo")
@RequiredArgsConstructor
@CustomControllerHandler
@Slf4j
public class UserPhotoController {

    private final UserPhotoService userPhotoService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApplicationResponse> getPhotoByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userPhotoService.getPhotoByUserId(userId));
    }

    @PutMapping("/{photoId}")
    public ResponseEntity<ApplicationResponse> updatePhoto(@PathVariable Long photoId, @Valid @RequestBody UserPhotoRequest request, BindingResult bindingResult) throws ExceptionValidatedRequestOrResponse {
        if (bindingResult.hasErrors()) {
            throw new ExceptionValidatedRequestOrResponse(bindingResult);
        }
        return ResponseEntity.ok(userPhotoService.updatePhotoByPhotoId(photoId, request));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        userPhotoService.deletePhotoByPhotoId(photoId);
        return ResponseEntity.ok().build();
    }
}