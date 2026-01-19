package com.example.bookstore.criteriaQuery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.domain.Pageable;
\
import org.thymeleaf.util.ObjectUtils;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;

@Data
public class AuthorQueryCriteria {
    @Query(blurry = "firstName,lastName,email")
    private String blurry;

    @Query(type = Type.BETWEEN, propName= "audit.createdDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Date> createdDate;

    @Query(type = Type.EQUAL)
    private Long id;

    private Pageable pageable;

}
