package com.example.bookstore.mapper;

import com.example.bookstore.dtos.AuthorDto;
import com.example.bookstore.models.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author author);

    List<AuthorDto> toDtoList(List<Author> authors);

    @Mapping(target = "id", ignore = true)
    Author toEntity(AuthorDto authorDto);
}
