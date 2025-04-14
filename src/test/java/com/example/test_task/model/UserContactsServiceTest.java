package com.example.test_task.model;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.request.user.UserContactsRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.dto.response.user.UserContactsResponse;
import com.example.test_task.mappers.UserContactsMapper;
import com.example.test_task.repository.UserContactsDAO;
import com.example.test_task.repository.UsersDAO;
import com.example.test_task.service.UserContactsServiceImpl;
import com.example.test_task.service.interfaces.UserContactsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserContactsServiceTest {

    @InjectMocks
    private UserContactsServiceImpl serviceImpl;

    private UserContactsService service;

    @Mock
    private UserContactsDAO userContactsDAO;
    @Mock
    private UsersDAO usersDAO;
    @Mock
    private UserContactsMapper mapper;

    private Users user;
    private UserContacts contact;
    private UserContactsRequest request;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);

        contact = new UserContacts();
        contact.setId(2L);
        contact.setOwner(user);

        request = new UserContactsRequest();
        request.setEmail("test@test.com");
        request.setPhone("+123456789");

        service = serviceImpl;
    }

    @Test
    void createContact_userExists_success() {
        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toEntity(request)).thenReturn(contact);
        ApplicationResponse response = service.createContact(1L, request).getBody();
        verify(userContactsDAO).save(contact);
        assertThat(response.getMessageStatus()).isEqualTo(MessageStatus.CREATED);
        assertThat(response.getUserId()).isEqualTo(1L);
    }

    @Test
    void createContact_userNotFound_error() {
        when(usersDAO.findById(1L)).thenReturn(Optional.empty());
        ApplicationResponse response = service.createContact(1L, request).getBody();
        assertThat(response.getMessageStatus()).isEqualTo(MessageStatus.ERROR);
        assertThat(response.getErrorList().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getContactsByUserId_userExists_success() {
        when(usersDAO.findById(1L)).thenReturn(Optional.of(user));
        when(userContactsDAO.findByUserId(1L)).thenReturn(List.of(contact));
        when(mapper.toResponseList(List.of(contact))).thenReturn(List.of(new UserContactsResponse()));
        ApplicationResponse response = service.getContactsByUserId(1L).getBody();
        assertThat(response.getMessageStatus()).isEqualTo(MessageStatus.OK);
        assertThat(response.getUsers()).hasSize(1);
    }

    @Test
    void getContactsByUserId_userNotFound_error() {
        when(usersDAO.findById(1L)).thenReturn(Optional.empty());
        ApplicationResponse response = service.getContactsByUserId(1L).getBody();
        assertThat(response.getMessageStatus()).isEqualTo(MessageStatus.ERROR);
        assertThat(response.getErrorList().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateContact_contactExists_success() {
        when(userContactsDAO.findById(2L)).thenReturn(Optional.of(contact));
        ApplicationResponse response = service.updateContact(2L, request).getBody();
        verify(mapper).updateFromDto(request, contact);
        verify(userContactsDAO).update(contact);
        assertThat(response.getMessageStatus()).isEqualTo(MessageStatus.OK);
    }

    @Test
    void updateContact_contactNotFound_error() {
        when(userContactsDAO.findById(2L)).thenReturn(Optional.empty());
        ApplicationResponse response = service.updateContact(2L, request).getBody();
        assertThat(response.getMessageStatus()).isEqualTo(MessageStatus.ERROR);
        assertThat(response.getErrorList().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deleteContact_contactExists_deletes() {
        when(userContactsDAO.findById(2L)).thenReturn(Optional.of(contact));
        service.deleteContact(2L);
        verify(userContactsDAO).delete(contact);
    }

    @Test
    void deleteContact_contactNotFound_warns() {
        when(userContactsDAO.findById(2L)).thenReturn(Optional.empty());
        service.deleteContact(2L);
        verify(userContactsDAO, never()).delete(any());
    }
}