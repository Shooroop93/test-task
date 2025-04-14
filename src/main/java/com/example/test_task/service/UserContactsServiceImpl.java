package com.example.test_task.service;

import com.example.test_task.constant.MessageStatus;
import com.example.test_task.dto.request.user.UserContactsRequest;
import com.example.test_task.dto.response.ApplicationResponse;
import com.example.test_task.dto.response.Errors;
import com.example.test_task.dto.response.user.UserContactsResponse;
import com.example.test_task.dto.response.user.UserResponse;
import com.example.test_task.mappers.UserContactsMapper;
import com.example.test_task.model.UserContacts;
import com.example.test_task.model.Users;
import com.example.test_task.repository.UserContactsDAO;
import com.example.test_task.repository.UsersDAO;
import com.example.test_task.service.interfaces.UserContactsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class UserContactsServiceImpl implements UserContactsService {

    private final UserContactsDAO userContactsDAO;
    private final UsersDAO usersDAO;
    private final UserContactsMapper mapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<ApplicationResponse> createContact(Long userId, UserContactsRequest request) {
        log.debug("Creating a contact from a query: {}", request);
        ApplicationResponse response = new ApplicationResponse();

        Optional<Users> user = usersDAO.findById(userId);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found", userId);
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), format("Пользователь с id '%d' не найден", userId)));
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }

        Users userModel = user.get();
        UserContacts contact = mapper.toEntity(request);
        contact.setOwner(userModel);
        userContactsDAO.save(contact);

        log.info("Contact successfully created for userId={}, contactId={}", userId, contact.getId());
        response.setMessageStatus(MessageStatus.CREATED);
        response.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApplicationResponse> getContactsByUserId(Long userId) {
        log.debug("Fetching contacts for userId={}", userId);
        ApplicationResponse response = new ApplicationResponse();

        Optional<Users> user = usersDAO.findById(userId);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found", userId);
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), format("Пользователь с id '%d' не найден", userId)));
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }

        List<UserContacts> contacts = userContactsDAO.findByUserId(userId);
        List<UserContactsResponse> contactsDto = mapper.toResponseList(contacts);

        response.setMessageStatus(MessageStatus.OK);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(userId);
        userResponse.setContacts(contactsDto);

        response.addUserToList(userResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<ApplicationResponse> updateContact(Long contactId, UserContactsRequest request) {
        log.debug("Updating contactId={} with data: {}", contactId, request);
        ApplicationResponse response = new ApplicationResponse();

        Optional<UserContacts> contact = userContactsDAO.findById(contactId);
        if (contact.isEmpty()) {
            log.warn("Contact with ID {} not found", contactId);
            response.setMessageStatus(MessageStatus.ERROR);
            response.setErrorList(new Errors(HttpStatus.NOT_FOUND.value(), format("Контакт с id '%d' не найден", contactId)));
            return ResponseEntity.status(response.getErrorList().getStatusCode()).body(response);
        }

        UserContacts userContacts = contact.get();
        mapper.updateFromDto(request, userContacts);
        userContactsDAO.update(userContacts);

        log.info("Contact with ID {} successfully updated", contactId);
        response.setMessageStatus(MessageStatus.OK);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteContact(Long contactId) {
        log.debug("The contact of user ID {} has been deleted:", contactId);
        Optional<UserContacts> contact = userContactsDAO.findById(contactId);
        contact.ifPresentOrElse(userContactsDAO::delete, () -> log.warn("No contact for deletion found: {}", contactId));
    }
}