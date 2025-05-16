package com.example.demo.dto.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import org.springframework.data.domain.Page;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static <T> PageDto<T> fromPage(Page<?> page, List<T> content) {
        return new PageDto<T>()
                .setContent(content)
                .setPageNumber(page.getNumber() + 1)
                .setPageSize(page.getSize())
                .setTotalElements(page.getTotalElements())
                .setTotalPages(page.getTotalPages())
                .setFirst(page.isFirst())
                .setLast(page.isLast());
    }
}