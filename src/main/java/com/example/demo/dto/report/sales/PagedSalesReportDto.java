package com.example.demo.dto.report.sales;

import com.example.demo.dto.api.PageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedSalesReportDto<T> {
    private PageDto<T> content;
    private SalesReportSummaryDto summary;
}
