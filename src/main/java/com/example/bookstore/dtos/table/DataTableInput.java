package com.example.bookstore.dtos.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import tools.jackson.databind.JsonNode;

import java.awt.print.Pageable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataTableInput<T> {

    private Integer pageIndex = 1;

    private Integer pageSize = 10;

    private String sortField;

    private String sortOrder;

    private JsonNode queryCriteria;

    private String searchValue;

    private Integer draw;

}
