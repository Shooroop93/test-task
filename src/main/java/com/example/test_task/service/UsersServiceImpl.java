package com.example.test_task.service;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.dto.response.Errors;
import com.example.test_task.dto.response.user.UserResponse;
import com.example.test_task.mappers.UsersMapper;
import com.example.test_task.model.Users;
import com.example.test_task.model.UsersPhoto;
import com.example.test_task.repository.UsersDAO;
import com.example.test_task.repository.UsersPhotoDAO;
import com.example.test_task.service.interfaces.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    @Value("${spring.url.useravatar}")
    private String defaultAvatarUrl;

    private final UsersDAO usersDAO;
    private final UsersMapper usersMapper;
    private final UsersPhotoDAO usersPhotoDAO;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<ApplicationResponse> createUser(UserRequest request) {
        log.debug("Creating a user from a query: {}", request);
        ApplicationResponse response = new ApplicationResponse();
        Users userEntity = usersMapper.toEntity(request);
        usersDAO.save(userEntity);

        UsersPhoto usersPhoto = new UsersPhoto(defaultAvatarUrl, userEntity);
        userEntity.setPhoto(usersPhoto);

        usersPhotoDAO.save(usersPhoto);

        response.setUserId(userEntity.getId());
        response.setMessageStatus(MessageStatus.CREATED);
        log.info("The user was successfully created with ID: {}", userEntity.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApplicationResponse> getAllUsers() {
        log.debug("Getting all users from the database");
        ApplicationResponse response = new ApplicationResponse();
        List<Users> users = usersDAO.findAll();

        if (users.isEmpty()) {
            log.debug("The user list is empty");
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase()));
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }

        response.setUsers(usersMapper.toDtoList(users));
        response.setMessageStatus(MessageStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApplicationResponse> getUserById(Long id) {
        log.debug("Search for a user by ID: {}", id);
        ApplicationResponse response = new ApplicationResponse();
        Optional<Users> user = usersDAO.findById(id);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found", id);
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), format("Пользователь с id '%d' не найден", id)));
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }
        UserResponse userDTO = usersMapper.toResponseDTO(user.get());
        response.addUserToList(userDTO);
        response.setMessageStatus(MessageStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void removeUser(Long id) {
        log.debug("Deleting a user with an ID: {}", id);
        Optional<Users> user = usersDAO.findById(id);
        user.ifPresentOrElse(usersDAO::deactivateUser, () -> log.warn("Пользователь для удаления не найден: {}", id));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<ApplicationResponse> updateUser(Long id, UserRequest request) {
        log.debug("Updating a user with ID {} data: {}", id, request);
        ApplicationResponse response = new ApplicationResponse();

        Optional<Users> user = usersDAO.findById(id);

        if (user.isEmpty()) {
            log.warn("User with ID {} not found for update", id);
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), format("Пользователь с id '%d' не найден", id)));
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }
        Users userDAO = user.get();
        usersMapper.updateUserFromDto(request, userDAO);
        usersDAO.update(userDAO);
        response.setMessageStatus(MessageStatus.OK);
        response.setUserId(userDAO.getId());
        log.info("User with ID {} has been successfully updated", id);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Users> getUserByLogin(String login) {
        return usersDAO.findByLogin(login);
    }
}