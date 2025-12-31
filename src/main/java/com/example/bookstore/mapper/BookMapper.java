package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(
            target="authorName",
            expression = "java(book.getAuthor().getFirstName() + \" \" + book.getAuthor().getLastName())"
    )
    BookDto toDto(Book book);

    Book toEntity(BookDto dto);


}
