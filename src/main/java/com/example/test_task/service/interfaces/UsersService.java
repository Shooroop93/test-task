package com.example.test_task.service.interfaces;

import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UsersService {

    ResponseEntity<ApplicationResponse> createUser(UserRequest request);

    ResponseEntity<ApplicationResponse> getAllUsers();

    ResponseEntity<ApplicationResponse> getUserById(Long id);

    ResponseEntity<ApplicationResponse> updateUser(Long id, UserRequest request);

    Optional<Users> getUserByLogin(String login);

    void removeUser(Long id);
}