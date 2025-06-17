package com.example.demo.service.category.impl;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CategoryImportDto;
import com.example.demo.dto.category.CreateCategoryDto;
import com.example.demo.dto.category.UpdateCategoryDto;
import com.example.demo.dto.data.BatchSaveResultDto;
import com.example.demo.dto.data.ImportResultDto;
import com.example.demo.dto.data.ParseResultDto;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.DataLogEntity;
import com.example.demo.enums.DataOperationStatusEnum;
import com.example.demo.enums.DataOperationsEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.ICategoryMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.DataLogRepository;
import com.example.demo.service.category.ICategoryService;
import com.example.demo.service.data.BatchSaveService;
import com.example.demo.service.data.ExcelParserService;
import com.example.demo.service.securityContext.impl.SecurityContextService;
import com.example.demo.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;
    private final ApiExceptionFactory apiExceptionFactory;
    private final MessageUtils messageUtils;
    private final ExcelParserService excelParserService;
    private final BatchSaveService batchSaveService;
    private final SecurityContextService securityContextService;
    private final ObjectMapper objectMapper;
    private final DataLogRepository dataLogRepository;

    @Override
    public ApiSuccessDto<PageDto<CategoryDto>> getAllCategories(int page, int size, String sortBy,
            String sortDirection) {
        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

        Page<CategoryEntity> categories = categoryRepository.findAll(pageable);
        PageDto<CategoryDto> categoriesDto = categoryMapper.toPageDto(categories);
        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.category.get.all.success"),
                categoriesDto);
    }

    @Override
    public ApiSuccessDto<CategoryDto> getCategoryById(int id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.category.get.by.id.not.found"));

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.category.get.by.id.success"),
                categoryMapper.toDto(category));
    }

    @Override
    public ApiSuccessDto<CategoryDto> createCategory(CreateCategoryDto category) {
        CategoryEntity categoryEntity = categoryMapper.toCreateEntity(category);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.category.create.success"),
                categoryMapper.toDto(savedCategory));
    }

    @Override
    public ApiSuccessDto<CategoryDto> updateCategory(int id, UpdateCategoryDto category) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.category.update.not.found"));

        categoryMapper.updateEntityFromDto(category, categoryEntity);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.category.update.success"),
                categoryMapper.toDto(savedCategory));
    }

    @Override
    public ApiSuccessDto<Void> deleteCategory(int id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.category.delete.not.found"));

        categoryRepository.delete(category);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.category.delete.success"));
    }

    @Override
    public ApiSuccessDto<ImportResultDto> importCategories(InputStream inputStream) {
        if (inputStream == null) {
            throw apiExceptionFactory.badRequestException("operation.category.import.invalid.file");
        }

        ParseResultDto<CategoryImportDto> parseResult = excelParserService.parseExcel(inputStream,
                CategoryImportDto.class);

        ImportResultDto finalResult = new ImportResultDto()
                .setImportId(parseResult.getParseId())
                .setTotalRows(parseResult.getTotalRows())
                .setErrorRows(parseResult.getErrorRows())
                .setSuccessRows(parseResult.getSuccessRows());

        if (parseResult.getEntities().isEmpty()) {
            throw apiExceptionFactory.badRequestException("operation.category.import.empty.file");
        }

        DataLogEntity dataLog = new DataLogEntity();
        dataLog.setJobType(DataOperationsEnum.IMPORT);
        dataLog.setEntityType(CategoryEntity.class.getSimpleName());
        dataLog.setStatus(DataOperationStatusEnum.STARTED);
        dataLog.setStartedAt(Instant.now());
        dataLog.setTriggeredBy(securityContextService.getUser());
        dataLog.setJobId(UUID.fromString(parseResult.getParseId()));

        dataLogRepository.save(dataLog);

        List<CategoryEntity> categoriesToSave = parseResult.getEntities().stream()
                .map(categoryMapper::toImportEntity)
                .collect(Collectors.toList());

        BatchSaveResultDto saveResult = batchSaveService.saveBatch(categoriesToSave, categoryRepository);

        finalResult.getErrors().addAll(parseResult.getErrors());
        finalResult.getErrors().addAll(saveResult.getErrors());

        if (saveResult.getErrorCount() > 0) {
            finalResult.setSuccessRows(saveResult.getSuccessCount());
            finalResult.setErrorRows(finalResult.getErrorRows() + saveResult.getErrorCount());
            dataLog.setStatus(DataOperationStatusEnum.COMPLETE_WITH_ERRORS);
        } else {
            dataLog.setStatus(DataOperationStatusEnum.SUCCESS);
        }

        try {
            dataLog.setDetails(objectMapper.readTree(objectMapper.writeValueAsString(finalResult)));
        } catch (Exception e) {
            apiExceptionFactory.businessException("operation.category.import.invalid.log.details", e.getMessage());
        }

        dataLog.setFinishedAt(Instant.now());
        dataLogRepository.save(dataLog);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.category.import.success"),
                finalResult);
    }

    @Async
    @Override
    public CompletableFuture<ApiSuccessDto<ImportResultDto>> importCategoriesAsync(InputStream inputStream) {
        return CompletableFuture.completedFuture(importCategories(inputStream));
    }

}
