package com.example.demo.dto.data;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ImportResultDto {
    private Integer totalRows = 0;
    private Integer successRows = 0;
    private Integer errorRows = 0;
    private List<String> errors = new ArrayList<>();
    private String importId;
}
