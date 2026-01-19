package com.example.bookstore.dtos.table;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public class DataTableOutput<T>{

    @JsonView(View.class)
    private int draw;

    @JsonView(View.class)
    private long recordsTotal = 0L;

    @JsonView(View.class)
    private long recordsFiltered = 0L;

    @JsonView(View.class)
    private List<T> data = Collections.emptyList();

    @JsonView(View.class)
    private String error;

    public interface View {}

    public static <E> DataTableOutput<E> of(Page<E> page){
        Objects.requireNonNull(page, "page must not be null");
        return build(page.getContent(), page.getTotalElements(), null);
    }

    public static <E> DataTableOutput<E> of(Page<E> page, long totalCount) {
        Objects.requireNonNull(page, "page must not be null");
        return build(page.getContent(), totalCount, null);
    }

    public static <E> DataTableOutput<E> of(List<E> list, long totalCount) {
        Objects.requireNonNull(list, "list must not be null");
        return build(list, totalCount, null);
    }


    public static <E> DataTableOutput<E> of(Page<E> page, DataTableInput input) {
        Objects.requireNonNull(page, "page must not be null");
        Objects.requireNonNull(input, "input must not be null");
        return build(page.getContent(), page.getTotalElements(), input.getDraw());
    }

    public static <E> DataTableOutput<E> of(List<E> list, long totalCount, DataTableInput input) {
        Objects.requireNonNull(list, "list must not be null");
        Objects.requireNonNull(input, "input must not be null");
        return build(list, totalCount, input.getDraw());
    }

    private static <E> DataTableOutput<E> build(List<E> data, long totalCount, Integer draw) {
        DataTableOutput<E> output = new DataTableOutput<>();
        output.setRecordsTotal(totalCount);
        output.setRecordsFiltered(totalCount);
        output.setData(data);

        if (draw != null) {
            output.setDraw(draw);
        }
        return output;
    }

}
