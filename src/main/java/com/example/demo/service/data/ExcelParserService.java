package com.example.demo.service.data;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.demo.dto.data.ParseResultDto;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.utils.DataUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelParserService {
    private final ApiExceptionFactory apiExceptionFactory;
    private final DataUtils dataUtils;

    public <T> ParseResultDto<T> parseExcel(InputStream inputStream, Class<T> entityClass) {
        String parseId = UUID.randomUUID().toString();
        ParseResultDto<T> result = new ParseResultDto<T>().setParseId(parseId);

        try {
            Map<String, Field> fieldMapping = dataUtils.createFieldMapping(entityClass);
            List<T> entities = processExcelFile(inputStream, entityClass, fieldMapping, result);
            result.setEntities(entities);
        } catch (Exception e) {
            throw apiExceptionFactory.dataImportException("exception.data.import.failed", e.getMessage());
        }

        return result;
    }

    private <T> List<T> processExcelFile(InputStream inputStream, Class<T> entityClass,
            Map<String, Field> fieldMapping, ParseResultDto<T> result) throws Exception {

        List<T> entities = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Row headerRow = sheet.getRow(0);
        Map<Integer, String> columnMapping = new HashMap<>();

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String columnName = cell.getStringCellValue().trim();
                columnMapping.put(i, columnName);
            }
        }

        int rowNum = 1;
        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;

            try {
                T entity = createEntityFromRow(row, entityClass, fieldMapping, columnMapping, rowNum);
                if (entity != null) {
                    entities.add(entity);
                    result.setSuccessRows(result.getSuccessRows() + 1);
                }
            } catch (Exception e) {
                result.getErrors().add("Row " + rowNum + ": " + e.getMessage());
                result.setErrorRows(result.getErrorRows() + 1);
            }
            rowNum++;
        }

        result.setTotalRows(rowNum - 1);
        workbook.close();
        return entities;
    }

    private <T> T createEntityFromRow(Row row, Class<T> entityClass,
            Map<String, Field> fieldMapping, Map<Integer, String> columnMapping, int rowNum)
            throws Exception {

        T entity = entityClass.getDeclaredConstructor().newInstance();

        for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            String columnName = columnMapping.get(cellIndex);

            if (columnName != null && fieldMapping.containsKey(columnName)) {
                Field field = fieldMapping.get(columnName);
                Object value = dataUtils.convertCellValue(cell, field, columnName, rowNum);

                if (value != null) {
                    field.set(entity, value);
                }
            }
        }

        dataUtils.validateEntity(entity, rowNum);
        return entity;
    }
}
