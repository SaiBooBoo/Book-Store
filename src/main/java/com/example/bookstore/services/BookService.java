package com.example.bookstore.services;

import com.example.bookstore.dtos.BookDto;
import com.example.bookstore.dtos.table.DataTableInput;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.models.Author;
import com.example.bookstore.models.Book;
import com.example.bookstore.repositories.AuthorRepository;
import com.example.bookstore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private BookMapper mapper;

    public List<BookDto> findAll() {
        List<Book> books = bookRepo.findAll();
        return mapper.toDtoList(books);
    }

    public BookDto findById(Long id) {
        Book book = bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Book id not found."));
        return mapper.toDto(book);
    }

    public BookDto createBook(BookDto dto) {
        Book book = mapper.toEntity(dto);

        Author author = authorRepo.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        book.setAuthor(author);
        try{
            bookRepo.save(book);
        } catch (Exception e) {
            throw new RuntimeException("Author not Found");
        }
        return mapper.toDto(book);
    }

    public void deleteById(Long id) {
        bookRepo.deleteById(id);
    }

    public Page<Book> search(String keyword, Pageable pageable) {
        return bookRepo
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword, pageable);
    }

    public Page<BookDto> findPaginated(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> bookPage = bookRepo.findAll(pageable);
        List<BookDto> dtoList = mapper.toDtoList(bookPage.getContent());

        return new PageImpl<>(
                dtoList,
                pageable,
                bookPage.getTotalElements()
        );
    }

    @Transactional
    public void updateBook(Long id, BookDto bookDto) {

        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book with that does not exist"));

        mapper.updateEntityFromDto(bookDto, book);

        Author author = authorRepo.findById(bookDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        book.setAuthor(author);
    }

//    public DataTableOutput<BookDto> findBooksDataTable(
//            DataTableInput<BookDto> input, Page<?> page, List<BookDto> data) {
//
//        AuthorService.getPageIndex(input.getPageIndex(), input.getPageSize(), input.getSortField(), input.getSortOrder(), input);
//
//        Page<Book> page;
//
//        if (hasSearch(input)) {
//            page = bookRepo.search(
//                    input.getSearchValue(),
//                    pageable
//            );
//        } else {
//            page = bookRepo.findAll(pageable);
//        }
//
//        List<BookDto> data = page
//                .getContent()
//                .stream()
//                .map(mapper::toDto)
//                .toList();
//
//        return buildResponse(input, page, data);
//    }

    private boolean hasSearch(DataTableInput<?> input) {
        return input.getSearchValue() != null && !input.getSearchValue().isBlank();
    }

//    private DataTableOutput<BookDto> buildResponse(
//            DataTableOutput<?> input, Page<?> page, List<BookDto> data
//    ){
//        DataTableOutput<BookDto> output = new DataTableOutput<>();
//
////        output.setDraw(input.getPageIndex());
//        output.setRecordsTotal(page.getTotalElements());
//        output.setRecordsFiltered(page.getTotalElements());
//        output.setData(data);
//        output.setError("");
//
//    }

}
