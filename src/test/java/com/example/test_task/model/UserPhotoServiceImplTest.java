package com.example.test_task.model;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.repository.UsersDAO;
import com.example.test_task.repository.UsersPhotoDAO;
import com.example.test_task.service.UserPhotoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserPhotoServiceImplTest {

    @InjectMocks
    private UserPhotoServiceImpl service;

    @Mock
    private UsersDAO usersDAO;

    @Mock
    private UsersPhotoDAO usersPhotoDAO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "uploadDir", "uploads");
        ReflectionTestUtils.setField(service, "defaultAvatarUrl", "http://default/avatar.png");
    }

    @Test
    void uploadPhoto_success() throws IOException {
        Users user = new Users();
        user.setId(1L);
        MultipartFile file = new MockMultipartFile("file", "avatar.jpg", "image/jpeg", "image data".getBytes());

        when(usersDAO.findById(1L, false)).thenReturn(Optional.of(user));
        doNothing().when(usersPhotoDAO).save(any());

        ResponseEntity<ApplicationResponse> response = service.uploadPhoto(1L, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MessageStatus.OK, response.getBody().getMessageStatus());
    }

    @Test
    void uploadPhoto_userNotFound() {
        MultipartFile file = new MockMultipartFile("file", "avatar.jpg", "image/jpeg", "image data".getBytes());

        when(usersDAO.findById(1L, false)).thenReturn(Optional.empty());

        ResponseEntity<ApplicationResponse> response = service.uploadPhoto(1L, file);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MessageStatus.ERROR, response.getBody().getMessageStatus());
    }

    @Test
    void uploadPhoto_invalidMimeType() {
        Users user = new Users();
        MultipartFile file = new MockMultipartFile("file", "virus.exe", "application/octet-stream", "bad".getBytes());

        when(usersDAO.findById(1L, false)).thenReturn(Optional.of(user));

        ResponseEntity<ApplicationResponse> response = service.uploadPhoto(1L, file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessageStatus.ERROR, response.getBody().getMessageStatus());
    }

    @Test
    void uploadPhoto_ioException() throws IOException {
        Users user = new Users();
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getOriginalFilename()).thenReturn("avatar.jpg");
        when(file.getBytes()).thenThrow(new IOException("Fail"));

        when(usersDAO.findById(1L, false)).thenReturn(Optional.of(user));

        ResponseEntity<ApplicationResponse> response = service.uploadPhoto(1L, file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessageStatus.ERROR, response.getBody().getMessageStatus());
    }

    @Test
    void downloadPhoto_success() throws IOException {
        Path file = Files.createTempFile("avatar", ".jpg");
        UsersPhoto photo = new UsersPhoto();
        photo.setUrlPhoto(file.toString());

        Users user = new Users();
        user.setPhoto(photo);

        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Resource> response = service.downloadPhoto(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void downloadPhoto_userNotFound() {
        when(usersDAO.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Resource> response = service.downloadPhoto(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void downloadPhoto_photoMissing() {
        Users user = new Users();
        user.setPhoto(null);

        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Resource> response = service.downloadPhoto(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void downloadPhoto_fileNotFound() {
        UsersPhoto photo = new UsersPhoto();
        photo.setUrlPhoto("nonexistent/path.jpg");

        Users user = new Users();
        user.setPhoto(photo);

        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Resource> response = service.downloadPhoto(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePhoto_success() throws IOException {
        Path file = Files.createTempFile("avatar", ".jpg");

        UsersPhoto photo = new UsersPhoto();
        photo.setId(10L);
        photo.setUrlPhoto(file.toString());

        when(usersPhotoDAO.findById(10L)).thenReturn(Optional.of(photo));

        ResponseEntity<ApplicationResponse> response = service.deletePhoto(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://default/avatar.png", photo.getUrlPhoto());
    }

    @Test
    void deletePhoto_notFound() {
        when(usersPhotoDAO.findById(10L)).thenReturn(Optional.empty());

        ResponseEntity<ApplicationResponse> response = service.deletePhoto(10L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MessageStatus.ERROR, response.getBody().getMessageStatus());
    }
}
