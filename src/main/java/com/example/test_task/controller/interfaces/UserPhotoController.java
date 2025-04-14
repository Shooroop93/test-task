package com.example.test_task.controller.interfaces;

import com.example.test_task.annotation.CustomControllerHandler;
import com.example.test_task.dto.request.user.UserPhotoRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserPhotoController {

    @GetMapping("/{userId}")
    ResponseEntity<ApplicationResponse> getPhotoByUserId(@PathVariable Long userId);

    @PutMapping("/{photoId}")
    @CustomControllerHandler
    ResponseEntity<ApplicationResponse> updatePhoto(@PathVariable Long photoId, @Valid @RequestBody UserPhotoRequest request);

    @DeleteMapping("/{photoId}")
    ResponseEntity<Void> deletePhoto(@PathVariable Long photoId);
}