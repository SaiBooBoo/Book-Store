package com.example.bookstore.dtos.table;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import tools.jackson.databind.JsonNode;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTableInput<T> {

    private Integer pageIndex = 1;
    private Integer pageSize = Integer.MAX_VALUE;

    @JsonProperty("queryCriteria")
    private JsonNode queryCriteria;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortField;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortOrder;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String searchValue;

    private Integer draw;

    public Pageable getPageable() {
        int startIndex = pageIndex - 1;
        Sort.Direction dir = Sort.Direction.valueOf(sortOrder.toUpperCase());
        return PageRequest.of(startIndex, pageSize, Sort.by(dir, sortField));
    }

}
