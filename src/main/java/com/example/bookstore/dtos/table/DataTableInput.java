package com.example.bookstore.dtos.table;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTableInput<T> {

    private Integer pageIndex = 1;
    private Integer pageSize = Integer.MAX_VALUE;

    @JsonProperty("queryCriteria")
    private T queryCriteria;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortField;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortOrder;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String searchValue;

    private Integer draw;

    public Integer getDraw() {
        return draw == null ? 1 : draw;
    }

    public Pageable getPageable() {

        int uiPage = pageIndex == null || pageIndex < 1 ? 1 : pageIndex;
        int page = uiPage - 1;
        int size = pageSize == null || pageSize <= 0 ? 10 : pageSize;

        if (sortField == null || sortOrder == null) {
            return PageRequest.of(page, size);
        }

        Sort.Direction dir =
                "descend".equalsIgnoreCase(sortOrder) || "desc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(dir, sortField));
    }

}
