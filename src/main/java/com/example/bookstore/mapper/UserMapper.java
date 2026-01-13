package com.example.bookstore.mapper;

import com.example.bookstore.dtos.UserDto;
import com.example.bookstore.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserDto toDto(User user);

    User toEntity(UserDto user);

    List<UserDto> toDtoList(List<User> users);
}
