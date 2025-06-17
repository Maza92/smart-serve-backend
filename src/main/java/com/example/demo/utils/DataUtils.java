package com.example.demo.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Component;

import com.example.demo.annotation.ImportColumn;
import com.example.demo.exception.ApiExceptionFactory;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataUtils {
    private final ApiExceptionFactory apiExceptionFactory;
    private final Validator validator;

    public Object convertCellValue(Cell cell, Field field, String columnName, int rowNum)
            throws Exception {

        if (cell == null) {
            ImportColumn annotation = field.getAnnotation(ImportColumn.class);
            if (annotation.required()) {
                throw apiExceptionFactory.validationException("exception.data.import.required.field", columnName);
            }
            return null;
        }

        Class<?> fieldType = field.getType();

        try {
            if (fieldType == String.class) {
                return getCellValueAsString(cell);
            } else if (fieldType == Integer.class || fieldType == int.class) {
                return (int) cell.getNumericCellValue();
            } else if (fieldType == Long.class || fieldType == long.class) {
                return (long) cell.getNumericCellValue();
            } else if (fieldType == Double.class || fieldType == double.class) {
                return cell.getNumericCellValue();
            } else if (fieldType == BigDecimal.class) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            } else if (fieldType == Date.class) {
                ImportColumn annotation = field.getAnnotation(ImportColumn.class);
                if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(annotation.dateFormat());
                    return sdf.parse(getCellValueAsString(cell));
                }
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                String value = getCellValueAsString(cell).toLowerCase();
                return value.equals("true") || value.equals("1") || value.equals("sí") || value.equals("si");
            } else if (fieldType.isEnum()) {
                String value = getCellValueAsString(cell).trim().toUpperCase();
                if (value.isEmpty()) {
                    return null;
                }
                return Enum.valueOf((Class<Enum>) fieldType, value);
            }
        } catch (Exception e) {
            throw apiExceptionFactory.dataImportException("exception.data.import.conversion.error", columnName,
                    e.getMessage());
        }

        return null;
    }

    public String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public <T> void validateEntity(T entity, int rowNum) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                errors.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw apiExceptionFactory.validationException("exception.data.import.validation.error", errors.toString());
        }
    }

    public <T> Map<String, Field> createFieldMapping(Class<T> entityClass) {
        Map<String, Field> mapping = new HashMap<>();
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ImportColumn.class)) {
                ImportColumn annotation = field.getAnnotation(ImportColumn.class);
                field.setAccessible(true);
                mapping.put(annotation.name(), field);
            }
        }

        return mapping;
    }
}
