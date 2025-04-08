package com.example.test_task.model;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.dto.response.user.UserResponse;
import com.example.test_task.mappers.UsersMapper;
import com.example.test_task.repository.UsersDAO;
import com.example.test_task.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @Mock
    private UsersDAO usersDAO;

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_success() {
        UserRequest request = new UserRequest();
        request.setFirstName("Test");
        request.setLastName("User");

        Users entity = new Users();
        entity.setId(1L);

        when(usersMapper.toEntity(request)).thenReturn(entity);

        ApplicationResponse response = usersService.createUser(request);

        verify(usersDAO).save(entity);
        assertEquals(MessageStatus.CREATED, response.getMessageStatus());
        assertEquals(1L, response.getUserId());
    }

    @Test
    void getAllUsers_empty() {
        when(usersDAO.findAll()).thenReturn(Collections.emptyList());

        ApplicationResponse response = usersService.getAllUsers();

        assertEquals(MessageStatus.ERROR, response.getMessageStatus());
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getErrorList().getStatusCode());
    }

    @Test
    void getAllUsers_success() {
        Users user = new Users();
        when(usersDAO.findAll()).thenReturn(Collections.singletonList(user));
        when(usersMapper.toDtoList(any())).thenReturn(Collections.singletonList(new UserResponse()));

        ApplicationResponse response = usersService.getAllUsers();

        assertEquals(MessageStatus.OK, response.getMessageStatus());
        assertNotNull(response.getUsers());
    }

    @Test
    void getUserById_notFound() {
        when(usersDAO.findById(1L)).thenReturn(Optional.empty());

        ApplicationResponse response = usersService.getUserById(1L);

        assertEquals(MessageStatus.ERROR, response.getMessageStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getErrorList().getStatusCode());
    }

    @Test
    void getUserById_success() {
        Users user = new Users();
        user.setId(1L);
        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));
        when(usersMapper.toResponseDTO(user)).thenReturn(new UserResponse());

        ApplicationResponse response = usersService.getUserById(1L);

        assertEquals(MessageStatus.OK, response.getMessageStatus());
        assertFalse(response.getUsers().isEmpty());
    }

    @Test
    void updateUser_notFound() {
        when(usersDAO.findById(1L)).thenReturn(Optional.empty());

        ApplicationResponse response = usersService.updateUser(1L, new UserRequest());

        assertEquals(MessageStatus.ERROR, response.getMessageStatus());
    }

    @Test
    void updateUser_success() {
        Users user = new Users();
        user.setId(1L);
        UserRequest request = new UserRequest();
        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));

        ApplicationResponse response = usersService.updateUser(1L, request);

        verify(usersMapper).updateUserFromDto(request, user);
        verify(usersDAO).update(user);
        assertEquals(MessageStatus.OK, response.getMessageStatus());
    }

    @Test
    void removeUser_found() {
        Users user = new Users();
        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));

        usersService.removeUser(1L);

        verify(usersDAO).delete(user);
    }

    @Test
    void removeUser_notFound() {
        when(usersDAO.findById(1L)).thenReturn(Optional.empty());

        usersService.removeUser(1L);

        verify(usersDAO, never()).delete(any());
    }
}
