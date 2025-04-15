package com.example.test_task.service;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.dto.response.Errors;
import com.example.test_task.model.Users;
import com.example.test_task.model.UsersPhoto;
import com.example.test_task.repository.UsersDAO;
import com.example.test_task.repository.UsersPhotoDAO;
import com.example.test_task.service.interfaces.UserPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPhotoServiceImpl implements UserPhotoService {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("${spring.url.useravatar}")
    private String defaultAvatarUrl;

    private final UsersDAO usersDAO;
    private final UsersPhotoDAO usersPhotoDAO;

    private final List<String> allowedMimeTypes = List.of("image/jpeg", "image/png");

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<ApplicationResponse> uploadPhoto(Long userId, MultipartFile file) {
        ApplicationResponse response = new ApplicationResponse();

        Optional<Users> optionalUser = usersDAO.findById(userId, false);
        if (optionalUser.isEmpty()) {
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), "User not found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (file.isEmpty() || !allowedMimeTypes.contains(file.getContentType())) {
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.BAD_REQUEST.value(), "Unsupported or empty file"));
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String ext = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            Path userDir = Path.of(uploadDir, "avatars", userId.toString());
            Files.createDirectories(userDir);

            String filename = UUID.randomUUID() + ext;
            Path filePath = userDir.resolve(filename);
            Files.write(filePath, file.getBytes());

            Users user = optionalUser.get();
            UsersPhoto photo = user.getPhoto();

            if (photo == null) {
                photo = new UsersPhoto();
                photo.setOwner(user);
            }

            photo.setUrlPhoto(filePath.toString().replace("\\", "/"));
            user.setPhoto(photo);
            usersPhotoDAO.save(photo);

            response.setMessageStatus(MessageStatus.OK);
            response.setUserId(userId);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Error saving photo: {}", e.getMessage(), e);
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not save file"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> downloadPhoto(Long userId) {
        Optional<Users> optionalUser = usersDAO.findById(userId);
        if (optionalUser.isEmpty()) return ResponseEntity.notFound().build();

        UsersPhoto photo = optionalUser.get().getPhoto();
        if (photo == null || photo.getUrlPhoto() == null) return ResponseEntity.notFound().build();

        Path path = Path.of(photo.getUrlPhoto());
        if (!Files.exists(path)) return ResponseEntity.notFound().build();

        Resource resource = new FileSystemResource(path);
        String contentType = path.toString().endsWith(".png") ? "image/png" : "image/jpeg";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"avatar\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @Override
    @Transactional
    public ResponseEntity<ApplicationResponse> deletePhoto(Long photoId) {
        ApplicationResponse response = new ApplicationResponse();

        Optional<UsersPhoto> photoOpt = usersPhotoDAO.findById(photoId);
        if (photoOpt.isEmpty()) {
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), "Photo not found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        UsersPhoto photo = photoOpt.get();
        try {
            Path path = Path.of(photo.getUrlPhoto());
            if (Files.exists(path)) Files.delete(path);
        } catch (IOException e) {
            log.warn("Could not delete file: {}", e.getMessage());
        }

        photo.setUrlPhoto(defaultAvatarUrl);
        usersPhotoDAO.update(photo);

        response.setMessageStatus(MessageStatus.OK);
        return ResponseEntity.ok(response);
    }
}