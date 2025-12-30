package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(
            target="authorName",
            expression = "java(book.getAuthor().getFirstName() + \" \" + book.getAuthor().getLastName())"
    )
    BookDto toDto(Book book);

//    @Mapping(target = "author", ignore = true)
    Book toEntity(BookDto dto);

//    @Mapping(target = "author", ignore = true)
//    void updateEntity(BookDto dto, @MappingTarget Book book);

//    @Named("authorName")
//    default String authorNameForDto(Book book) {
//        return book.getAuthor().getFirstName();
//    }
}
