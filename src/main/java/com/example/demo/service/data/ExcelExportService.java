package com.example.demo.service.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.demo.utils.DataUtils;
import com.example.demo.utils.ExcelUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelExportService {
    private final ExcelUtils excelUtils;
    private final DataUtils dataUtils;

    public <T> byte[] exportToExcel(List<T> data, Class<T> entityClass, String sheetName)
            throws IOException {

        log.info("Starting Excel file processing");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle headerStyle = excelUtils.createHeaderStyle(workbook);
        CellStyle dataStyle = excelUtils.createDataStyle(workbook);

        Map<String, Field> fieldMapping = dataUtils.createFieldMapping(entityClass);
        List<String> headers = new ArrayList<>(fieldMapping.keySet());

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }

        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex + 1);
            T entity = data.get(rowIndex);

            for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                String header = headers.get(colIndex);
                Field field = fieldMapping.get(header);

                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);

                    Cell cell = row.createCell(colIndex);
                    excelUtils.setCellValue(cell, value);
                    cell.setCellStyle(dataStyle);

                } catch (IllegalAccessException e) {
                    log.error("Error setting cell value for column '{}', row {}: {}", header, rowIndex, e.getMessage());
                }
            }
        }

        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
