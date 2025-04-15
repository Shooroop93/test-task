package com.example.test_task.controller.interfaces;

import com.example.test_task.dto.response.ApplicationResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

public interface UserPhotoController {

    @PostMapping("/upload/{userId}")
    ResponseEntity<ApplicationResponse> uploadPhoto(@PathVariable Long userId, MultipartFile file);

    @GetMapping("/download/{userId}")
    ResponseEntity<Resource> downloadPhoto(@PathVariable Long userId);

    @DeleteMapping("/delete/{photoId}")
    ResponseEntity<ApplicationResponse> deletePhoto(@PathVariable Long photoId);
}