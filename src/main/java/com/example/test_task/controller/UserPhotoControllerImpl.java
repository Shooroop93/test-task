package com.example.test_task.controller;

import com.example.test_task.controller.interfaces.UserPhotoController;
import com.example.test_task.dto.request.user.UserPhotoRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.service.interfaces.UserPhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/photo")
@RequiredArgsConstructor
@Slf4j
public class UserPhotoControllerImpl implements UserPhotoController {

    private final UserPhotoService userPhotoService;

    @Override
    public ResponseEntity<ApplicationResponse> getPhotoByUserId(@PathVariable Long userId) {
        return userPhotoService.getPhotoByUserId(userId);
    }

    @Override
    public ResponseEntity<ApplicationResponse> updatePhoto(@PathVariable Long photoId, @Valid @RequestBody UserPhotoRequest request) {
        return userPhotoService.updatePhotoByPhotoId(photoId, request);
    }

    @Override
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        userPhotoService.deletePhotoByPhotoId(photoId);
        return ResponseEntity.ok().build();
    }
}