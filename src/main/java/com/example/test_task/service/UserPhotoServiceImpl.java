package com.example.test_task.service;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.request.user.UserPhotoRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.dto.response.Errors;
import com.example.test_task.dto.response.user.UserPhotoResponse;
import com.example.test_task.dto.response.user.UserResponse;
import com.example.test_task.mappers.UsersPhotoMapper;
import com.example.test_task.model.Users;
import com.example.test_task.model.UsersPhoto;
import com.example.test_task.repository.UsersDAO;
import com.example.test_task.repository.UsersPhotoDAO;
import com.example.test_task.service.interfaces.UserPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPhotoServiceImpl implements UserPhotoService {

    @Value("${spring.url.useravatar}")
    private String defaultAvatarUrl;

    private final UsersPhotoDAO usersPhotoDAO;
    private final UsersDAO usersDAO;
    private final UsersPhotoMapper usersPhotoMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApplicationResponse> getPhotoByUserId(Long userId) {
        log.debug("Fetching contacts for userId={}", userId);
        ApplicationResponse response = new ApplicationResponse();

        Optional<Users> user = usersDAO.findById(userId);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found", userId);
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), format("Пользователь с id '%d' не найден", userId)));
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }

        UserPhotoResponse userPhotoResponse = usersPhotoMapper.toResponseDTO(user.get().getPhoto());

        response.setMessageStatus(MessageStatus.OK);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(userId);
        userResponse.setAvatar(userPhotoResponse);

        response.addUserToList(userResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<ApplicationResponse> updatePhotoByPhotoId(Long photoId, UserPhotoRequest request) {
        ApplicationResponse response = new ApplicationResponse();
        Optional<UsersPhoto> usersPhoto = usersPhotoDAO.findById(photoId);
        if (usersPhoto.isEmpty()) {
            log.warn("Photo with ID {} not found", photoId);
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), format("Фото с id '%d' не найдена", photoId)));
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }

        usersPhotoMapper.updateUserFromDto(request, usersPhoto.get());

        log.info("Photo with ID {} successfully updated", photoId);
        response.setMessageStatus(MessageStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deletePhotoByPhotoId(Long photoId) {
        log.debug("The photo of photoId {} has been deleted:", photoId);
        Optional<UsersPhoto> photo = usersPhotoDAO.findById(photoId);
        photo.ifPresentOrElse(
                p -> {
                    p.setUrlPhoto(defaultAvatarUrl);
                    usersPhotoDAO.update(p);
                },
                () -> log.warn("Фото для удаления не найдено: {}", photoId)
        );
    }
}