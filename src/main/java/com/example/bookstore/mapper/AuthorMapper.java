package com.example.bookstore.mapper;

import com.example.bookstore.dto.AuthorDetailDto;
import com.example.bookstore.dto.AuthorListDto;
import com.example.bookstore.models.Author;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorListDto toListDto(Author author);

    AuthorDetailDto toDetailDto(Author author);
}
