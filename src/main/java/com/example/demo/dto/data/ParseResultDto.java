package com.example.demo.dto.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ParseResultDto<T> {
    private String parseId;
    private List<T> entities = new ArrayList<>();
    private int totalRows;
    private int successRows;
    private int errorRows;
    private List<String> errors = new ArrayList<>();
}
