package com.example.test_task.service.interfaces;

import com.example.test_task.dto.response.ApplicationResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserPhotoService {

    ResponseEntity<ApplicationResponse> uploadPhoto(Long userId, MultipartFile file);

    ResponseEntity<Resource> downloadPhoto(Long userId);

    ResponseEntity<ApplicationResponse> deletePhoto(Long photoId);
}