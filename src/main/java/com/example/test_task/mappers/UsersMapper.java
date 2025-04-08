package com.example.test_task.mappers;

import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "id", ignore = true)
    Users toEntity(UserRequest dto);

    UserRequest toDto(Users dish);
}