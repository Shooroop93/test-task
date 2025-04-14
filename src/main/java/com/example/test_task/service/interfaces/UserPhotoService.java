package com.example.test_task.service.interfaces;

import com.example.test_task.dto.request.user.UserPhotoRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import org.springframework.http.ResponseEntity;

public interface UserPhotoService {

    ResponseEntity<ApplicationResponse> getPhotoByUserId(Long userId);

    ResponseEntity<ApplicationResponse> updatePhotoByPhotoId(Long photoId, UserPhotoRequest request);

    void deletePhotoByPhotoId(Long photoId);
}