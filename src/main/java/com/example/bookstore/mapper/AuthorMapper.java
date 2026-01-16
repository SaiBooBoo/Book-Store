package com.example.bookstore.mapper;

import com.example.bookstore.dtos.AuthorDto;
import com.example.bookstore.models.Author;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author author);

    List<AuthorDto> toDtoList(List<Author> authors);

    Author toAuthorDetail(AuthorDto authorDto);
}
