package com.example.test_task.mappers;

import com.example.test_task.dto.request.user.UserContactsRequest;
import com.example.test_task.dto.response.user.UserContactsResponse;
import com.example.test_task.model.UserContacts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserContactsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    UserContacts toEntity(UserContactsRequest dto);

    @Mapping(source = "owner.id", target = "userId")
    UserContactsResponse toResponse(UserContacts contact);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateFromDto(UserContactsRequest dto, @MappingTarget UserContacts entity);

    List<UserContactsResponse> toResponseList(List<UserContacts> contacts);
}