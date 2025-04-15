package com.example.test_task.controller;

import com.example.test_task.controller.interfaces.UserPhotoController;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.service.interfaces.UserPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/user/photo")
@RequiredArgsConstructor
@Slf4j
public class UserPhotoControllerImpl implements UserPhotoController {

    private final UserPhotoService userPhotoService;

    @Override
    public ResponseEntity<ApplicationResponse> uploadPhoto(Long userId, MultipartFile file) {
        return userPhotoService.uploadPhoto(userId, file);
    }

    @Override
    public ResponseEntity<Resource> downloadPhoto(Long userId) {
        return userPhotoService.downloadPhoto(userId);
    }

    @Override
    public ResponseEntity<ApplicationResponse> deletePhoto(Long photoId) {
        return userPhotoService.deletePhoto(photoId);
    }
}