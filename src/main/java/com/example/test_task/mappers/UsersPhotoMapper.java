package com.example.test_task.mappers;

import com.example.test_task.dto.request.user.UserPhotoRequest;
import com.example.test_task.dto.response.user.UserPhotoResponse;
import com.example.test_task.model.UsersPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsersPhotoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    UsersPhoto toEntity(UserPhotoRequest dto);

    UserPhotoResponse toResponseDTO(UsersPhoto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateUserFromDto(UserPhotoRequest dto, @MappingTarget UsersPhoto entity);
}