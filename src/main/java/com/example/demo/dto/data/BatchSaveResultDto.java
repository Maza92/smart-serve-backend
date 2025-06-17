package com.example.demo.dto.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BatchSaveResultDto {
    private int successCount;
    private int errorCount;
    private int batchCount;
    private List<String> errors = new ArrayList<>();
}
