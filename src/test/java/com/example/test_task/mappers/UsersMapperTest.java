package com.example.test_task.mappers;

import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.dto.response.user.UserResponse;
import com.example.test_task.model.Users;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsersMapperTest {

    private final UsersMapper  mapper = Mappers.getMapper(UsersMapper.class);

    @Test
    void testToEntity() {
        UserRequest dto = new UserRequest();
        dto.setUserId(1L);
        dto.setFirstName("Test1");
        dto.setLastName("Test$");
        dto.setMiddleName("Test ");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));

        Users user = mapper.toEntity(dto);

        assertEquals(dto.getUserId(), user.getId());
        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.getMiddleName(), user.getMiddleName());
        assertEquals(dto.getBirthDate(), user.getBirthDate());
    }

    @Test
    void testToResponseDTO() {
        Users user = new Users();
        user.setId(1L);
        user.setFirstName("T");
        user.setLastName(" ");
        user.setMiddleName("!");
        user.setBirthDate(LocalDate.of(2025, 4, 8));

        UserResponse response = mapper.toResponseDTO(user);

        assertEquals(user.getId(), response.getUserId());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals(user.getMiddleName(), response.getMiddleName());
        assertEquals(user.getBirthDate(), response.getBirthDate());
    }

    @Test
    void testToDtoList() {
        Users user = new Users();
        user.setId(2L);
        user.setFirstName(" ");
        user.setLastName("@#% ");

        List<UserResponse> responses = mapper.toDtoList(List.of(user));

        assertEquals(1, responses.size());
        assertEquals(user.getId(), responses.getFirst().getUserId());
        assertEquals(user.getFirstName(), responses.getFirst().getFirstName());
    }

    @Test
    void testUpdateUserFromDto() {
        UserRequest dto = new UserRequest();
        dto.setFirstName("UpdatedName");
        dto.setLastName("UpdatedLast");
        dto.setMiddleName("UpdatedMid");
        dto.setBirthDate(LocalDate.of(2000, 2, 2));

        Users entity = new Users();
        entity.setId(99L);
        mapper.updateUserFromDto(dto, entity);

        assertEquals("UpdatedName", entity.getFirstName());
        assertEquals("UpdatedLast", entity.getLastName());
        assertEquals("UpdatedMid", entity.getMiddleName());
        assertEquals(LocalDate.of(2000, 2, 2), entity.getBirthDate());
        assertEquals(99L, entity.getId());
    }
}