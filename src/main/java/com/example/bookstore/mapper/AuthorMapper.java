package com.example.bookstore.mapper;

import com.example.bookstore.dto.AuthorDetailDto;
import com.example.bookstore.models.Author;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDetailDto toDetailDto(Author author);

    List<AuthorDetailDto> toDtoList(List<Author> authors);

    Author toAuthorDetail(AuthorDetailDto authorDto);
}
