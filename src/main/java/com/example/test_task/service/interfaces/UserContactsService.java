package com.example.test_task.service.interfaces;

import com.example.test_task.dto.request.user.UserContactsRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import org.springframework.http.ResponseEntity;

public interface UserContactsService {

    ResponseEntity<ApplicationResponse> createContact(Long userId, UserContactsRequest request);

    ResponseEntity<ApplicationResponse> getContactsByUserId(Long userId);

    ResponseEntity<ApplicationResponse> updateContact(Long contactId, UserContactsRequest request);

    void deleteContact(Long contactId);
}