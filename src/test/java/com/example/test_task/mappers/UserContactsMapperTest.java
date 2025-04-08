package com.example.test_task.mappers;

import com.example.test_task.dto.request.user.UserContactsRequest;
import com.example.test_task.dto.response.user.UserContactsResponse;
import com.example.test_task.model.UserContacts;
import com.example.test_task.model.Users;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserContactsMapperTest {

    private final UserContactsMapper mapper = Mappers.getMapper(UserContactsMapper.class);

    @Test
    public void toEntity_shouldMapFieldsCorrectly() {
        UserContactsRequest request = new UserContactsRequest();
        request.setEmail("test@example.com");
        request.setPhone("+1234567890");

        UserContacts entity = mapper.toEntity(request);

        assertThat(entity.getEmail()).isEqualTo("test@example.com");
        assertThat(entity.getPhone()).isEqualTo("+1234567890");
        assertThat(entity.getId()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getOwner()).isNull();
    }

    @Test
    public void toResponse_shouldMapFieldsCorrectly() {
        UserContacts contact = new UserContacts();
        contact.setId(1L);
        contact.setEmail("test@example.com");
        contact.setPhone("+1234567890");

        Users owner = new Users();
        owner.setId(99L);
        contact.setOwner(owner);

        UserContactsResponse response = mapper.toResponse(contact);

        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getPhone()).isEqualTo("+1234567890");
        assertThat(response.getUserId()).isEqualTo(99L);
    }

    @Test
    public void updateFromDto_shouldUpdateFieldsCorrectly() {
        UserContactsRequest request = new UserContactsRequest();
        request.setEmail("new@example.com");
        request.setPhone("+9876543210");

        UserContacts entity = new UserContacts();
        entity.setEmail("old@example.com");
        entity.setPhone("+1111111111");

        mapper.updateFromDto(request, entity);

        assertThat(entity.getEmail()).isEqualTo("new@example.com");
        assertThat(entity.getPhone()).isEqualTo("+9876543210");
    }

    @Test
    public void toResponseList_shouldMapListCorrectly() {
        UserContacts contact1 = new UserContacts();
        contact1.setEmail("email1@example.com");
        contact1.setPhone("+10000000001");
        Users owner1 = new Users();
        owner1.setId(1L);
        contact1.setOwner(owner1);

        UserContacts contact2 = new UserContacts();
        contact2.setEmail("email2@example.com");
        contact2.setPhone("+10000000002");
        Users owner2 = new Users();
        owner2.setId(2L);
        contact2.setOwner(owner2);

        List<UserContactsResponse> responses = mapper.toResponseList(List.of(contact1, contact2));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getUserId()).isEqualTo(1L);
        assertThat(responses.get(0).getEmail()).isEqualTo("email1@example.com");
        assertThat(responses.get(0).getPhone()).isEqualTo("+10000000001");
        assertThat(responses.get(1).getUserId()).isEqualTo(2L);
        assertThat(responses.get(1).getEmail()).isEqualTo("email2@example.com");
        assertThat(responses.get(1).getPhone()).isEqualTo("+10000000002");
    }
}