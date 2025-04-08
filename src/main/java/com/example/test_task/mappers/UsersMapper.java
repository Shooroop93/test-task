package com.example.test_task.mappers;

import com.example.test_task.dto.request.user.UserRequest;
import com.example.test_task.dto.response.user.UserResponse;
import com.example.test_task.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    @Mapping(target = "id", source = "userId")
    Users toEntity(UserRequest dto);

    @Mapping(source = "photo", target = "avatar")
    @Mapping(source = "id", target = "userId")
    UserResponse toResponseDTO(Users entity);

    List<UserResponse> toDtoList(List<Users> users);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserRequest dto, @MappingTarget Users entity);
}