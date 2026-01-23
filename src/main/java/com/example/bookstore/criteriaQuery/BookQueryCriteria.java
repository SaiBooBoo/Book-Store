package com.example.bookstore.criteriaQuery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.domain.Pageable;


import java.util.Date;
import java.util.List;

@Data
public class BookQueryCriteria {

    @Query(blurry = "title,isbn,price,stock")
    private String blurry;

    @Query(type = Type.BETWEEN)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private List<Date> createdDate;

    @Query(type = Type.EQUAL)
    private Long id;

    private Pageable pageable;
}
