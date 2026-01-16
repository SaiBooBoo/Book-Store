package com.example.bookstore.services;

import com.example.bookstore.dtos.AuthorDto;
import com.example.bookstore.dtos.table.DataTableInput;
import com.example.bookstore.dtos.table.DataTableOutput;
import com.example.bookstore.exceptions.AuthorHasBookException;
import com.example.bookstore.exceptions.DuplicateEmailException;
import com.example.bookstore.filter.AuthorFilter;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.models.Author;
import com.example.bookstore.repositories.AuthorRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository repo;
    private final AuthorMapper mapper;
    private final AuthorMapper authorMapper;

    public AuthorService(
            AuthorRepository repository, AuthorMapper mapper, AuthorMapper authorMapper) {
        this.repo = repository;
        this.mapper = mapper;
        this.authorMapper = authorMapper;
    }

    @Transactional(readOnly = true)
    public AuthorDto findById(Long id) {
        Author author = repo.findById(id).orElseThrow(() -> new RuntimeException("Author id not found."));
        return mapper.toDto(author);
    }

    public List<AuthorDto> findAllAuthors() {
        List<Author> authors = repo.findAll();
        return mapper.toDtoList(authors);
    }

    public Author save(AuthorDto authorDto)
    {
        Author author = mapper.toAuthorDetail(authorDto);
       if(author.getId() == null) {
           if(repo.existsByEmail(author.getEmail()) || repo.existsByEmailAndIdNot(author.getEmail(), author.getId()) ) {
               throw new DuplicateEmailException("Email already exists");
           }
       }
       return repo.save(author);
    }

    @Transactional
    public void deleteById(Long id) {
        Author author = repo.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));

        if (!author.getBooks().isEmpty()) {
            throw new AuthorHasBookException(
                    "Cannot delete author who still has books"
            );
        }
        repo.delete(author);
    }

    public Page<AuthorDto> findPaginated(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Author> authorPage = repo.findAll(pageable);
        List<AuthorDto> dtoList = mapper.toDtoList(authorPage.getContent());

        return new PageImpl<>(
                dtoList,
                pageable,
                authorPage.getTotalElements()
        );
    }

    public void exportAuthorToExcel(List<Author> authors, OutputStream outputStream) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Authors");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Last Name");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Date Of Birth");
        headerRow.createCell(5).setCellValue("Bio");

        int rowIndex = 1;

        for (Author author: authors) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(author.getId());
            row.createCell(1).setCellValue(author.getFirstName());
            row.createCell(2).setCellValue(author.getLastName());
            row.createCell(3).setCellValue(author.getEmail());
            row.createCell(4).setCellValue(
                    author.getDateOfBirth() != null ? author.getDateOfBirth().toString() : ""
            );
            row.createCell(5).setCellValue(author.getBio());
        }

        int columnCount = headerRow.getPhysicalNumberOfCells();
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(outputStream);
        workbook.close();
    }


    public DataTableOutput<AuthorDto> findAuthorsDataTable(
            DataTableInput input
    ) {

        int uiPage = input.getPageIndex() == null ? 1 : input.getPageIndex();
        int page = Math.max(uiPage - 1, 0);
        int size = input.getPageSize() == null ? 10: input.getPageSize();

        Sort sort = Sort.unsorted();
        if (input.getSortField() != null && input.getSortOrder() != null) {
            Sort.Direction dir =
                    ("descend".equalsIgnoreCase(input.getSortOrder()) || "desc".equalsIgnoreCase(input.getSortOrder()))
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;
            sort = Sort.by(dir, input.getSortField());
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Author> pageResult;

        if (input.getSearchValue() != null && !input.getSearchValue().isBlank()) {
            pageResult = repo.search(input.getSearchValue(), pageable);
        } else {
            pageResult = repo.findAll(pageable);
        }

        List<AuthorDto> dtos = pageResult.getContent().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());

        Page<AuthorDto> dtoPage = pageResult.map(authorMapper::toDto);
        return DataTableOutput.of(dtoPage, input);

    }

    static void getPageIndex(int pageIndex2, int pageSize2, String sortField, String sortOrder, DataTableInput<AuthorFilter> input) {
        int pageIndex = Math.max(pageIndex2 - 1, 0);
        int pageSize = pageSize2;

        Sort sort = Sort.unsorted();

        if (sortField != null && sortOrder != null){
            Sort.Direction direction = "descend".equalsIgnoreCase(sortOrder)
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            sort = Sort.by(direction, sortField);
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
    }

    private boolean hasSearch(DataTableInput<?> input) {
        return input.getSearchValue() != null && !input.getSearchValue().isBlank();
    }

    private DataTableOutput<AuthorDto> buildResponse(
            DataTableInput<?> input, Page<?> page, List<AuthorDto> data
    ) {
        DataTableOutput<AuthorDto> output = new DataTableOutput<>();

        output.setDraw(input.getPageIndex());
        output.setRecordsTotal(page.getTotalElements());
        output.setRecordsFiltered(page.getTotalElements());
        output.setData(data);
        output.setError("");

        return output;
    }
}
