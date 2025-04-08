package com.example.test_task.model;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.request.user.UserPhotoRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.dto.response.user.UserPhotoResponse;
import com.example.test_task.mappers.UsersPhotoMapper;
import com.example.test_task.repository.UsersDAO;
import com.example.test_task.repository.UsersPhotoDAO;
import com.example.test_task.service.UserPhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserPhotoServiceTest {

    @Mock
    private UsersPhotoDAO usersPhotoDAO;

    @Mock
    private UsersDAO usersDAO;

    @Mock
    private UsersPhotoMapper usersPhotoMapper;

    @InjectMocks
    private UserPhotoService userPhotoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPhotoByUserId_success() {
        Users user = new Users();
        user.setId(1L);
        UsersPhoto photo = new UsersPhoto();
        photo.setId(10L);
        photo.setUrlPhoto("http://example.com/photo.jpg");
        user.setPhoto(photo);

        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));
        when(usersPhotoMapper.toResponseDTO(photo)).thenReturn(new UserPhotoResponse(10L, "http://example.com/photo.jpg"));

        ApplicationResponse response = userPhotoService.getPhotoByUserId(1L);

        assertEquals(MessageStatus.OK, response.getMessageStatus());
        assertEquals(1L, response.getUsers().get(0).getUserId());
        assertEquals("http://example.com/photo.jpg", response.getUsers().get(0).getAvatar().getUrlPhoto());
    }

    @Test
    void getPhotoByUserId_userNotFound() {
        when(usersDAO.findById(1L)).thenReturn(Optional.empty());

        ApplicationResponse response = userPhotoService.getPhotoByUserId(1L);

        assertEquals(MessageStatus.ERROR, response.getMessageStatus());
    }

    @Test
    void updatePhotoByPhotoId_success() {
        UsersPhoto photo = new UsersPhoto();
        photo.setId(10L);

        UserPhotoRequest request = new UserPhotoRequest();
        request.setUrlPhoto("http://new-photo.com/avatar.png");

        when(usersPhotoDAO.findById(10L)).thenReturn(Optional.of(photo));

        ApplicationResponse response = userPhotoService.updatePhotoByPhotoId(10L, request);

        verify(usersPhotoMapper).updateUserFromDto(request, photo);
        assertEquals(MessageStatus.OK, response.getMessageStatus());
    }

    @Test
    void updatePhotoByPhotoId_notFound() {
        when(usersPhotoDAO.findById(10L)).thenReturn(Optional.empty());

        UserPhotoRequest request = new UserPhotoRequest();
        request.setUrlPhoto("http://new-photo.com/avatar.png");

        ApplicationResponse response = userPhotoService.updatePhotoByPhotoId(10L, request);

        assertEquals(MessageStatus.ERROR, response.getMessageStatus());
    }

    @Test
    void deletePhotoByPhotoId_success() {
        String defaultUrl = "http://тест.ру/default.png";
        UsersPhoto photo = new UsersPhoto();
        photo.setId(10L);
        photo.setUrlPhoto("http://photo.com/old.png");

        when(usersPhotoDAO.findById(10L)).thenReturn(Optional.of(photo));

        ReflectionTestUtils.setField(userPhotoService, "defaultAvatarUrl", defaultUrl);
        userPhotoService.deletePhotoByPhotoId(10L);
        assertEquals(defaultUrl, photo.getUrlPhoto());
    }
}