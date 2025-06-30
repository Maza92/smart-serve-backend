package com.example.demo.service.user.impl;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.ApiSuccessDto;
import com.example.demo.dto.api.PageDto;
import com.example.demo.dto.data.BatchSaveResultDto;
import com.example.demo.dto.data.ImportResultDto;
import com.example.demo.dto.data.ParseResultDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserImportDto;
import com.example.demo.entity.DataLogEntity;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.DataOperationStatusEnum;
import com.example.demo.enums.DataOperationsEnum;
import com.example.demo.enums.RoleEnum;
import com.example.demo.exception.ApiExceptionFactory;
import com.example.demo.mappers.IUserMapper;
import com.example.demo.repository.DataLogRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.data.BatchSaveService;
import com.example.demo.service.data.ExcelParserService;
import com.example.demo.service.securityContext.impl.SecurityContextService;
import com.example.demo.service.user.IUserService;
import com.example.demo.specifications.UserSpecifications;
import com.example.demo.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DataLogRepository dataLogRepository;
    private final IUserMapper userMapper;
    private final MessageUtils messageUtils;
    private final ApiExceptionFactory apiExceptionFactory;
    private final ExcelParserService excelParserService;
    private final BatchSaveService batchSaveService;
    private final SecurityContextService securityContextService;
    private final ObjectMapper objectMapper;

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public ApiSuccessDto<PageDto<UserDto>> getAllUsers(int page, int size, String search, String status, String role,
            String sortBy,
            String sortDirection) {

        if (size <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.size");

        if (page <= 0)
            throw apiExceptionFactory.badRequestException("operation.get.all.invalid.page.number");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);

        Specification<UserEntity> spec = Specification.where(null);

        if (search != null && !search.isBlank()) {
            spec = spec.and(UserSpecifications.nameOrEmailContains(search));
        }

        if (status != null && !status.isBlank()) {
            spec = spec.and(UserSpecifications.activeEquals(status));
        }

        if (role != null && !role.isBlank()) {
            spec = spec.and(UserSpecifications.roleEquals(role));
        }

        Page<UserEntity> users = userRepository.findAll(spec, pageable);

        List<UserDto> usersDto = users.getContent().stream()
                .map(userMapper::toDto)
                .toList();

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.user.get.all.success"),
                PageDto.fromPage(users, usersDto));
    }

    @Override
    public ApiSuccessDto<UserDto> getUserById(int id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.user.get.by.id.not.found"));

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.user.get.by.id.success"),
                userMapper.toDto(user));
    }

    @Override
    public ApiSuccessDto<UserDto> updateUser(int id, UserDto userDto) {
        System.out.println(userDto.toString());
        System.out.println(userDto.getPhone());
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> apiExceptionFactory.entityNotFound("operation.user.update.not.found"));

        System.out.println(userDto.toString());

        UserEntity updatedUser = userMapper.updateEntityFromDto(userDto, user);
        System.out.println(updatedUser.toString());

        if (!userDto.getRoleName().equals(user.getRole().getName().name())) {
            RoleEntity role = roleRepository.findByName(RoleEnum.valueOf(userDto.getRoleName().toUpperCase()))
                    .orElseThrow(() -> apiExceptionFactory.entityNotFound("role.not.found"));
            updatedUser.setRole(role);
        }

        System.out.println(updatedUser.toString());

        userRepository.save(user);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.user.update.success"),
                userMapper.toDto(user));
    }

    @Override
    public ApiSuccessDto<ImportResultDto> importUsers(InputStream inputStream) {

        if (inputStream == null) {
            throw apiExceptionFactory.badRequestException("operation.user.import.invalid.file");
        }

        ParseResultDto<UserImportDto> parseResult = excelParserService.parseExcel(inputStream, UserImportDto.class);

        ImportResultDto finalResult = new ImportResultDto()
                .setImportId(parseResult.getParseId())
                .setTotalRows(parseResult.getTotalRows())
                .setErrorRows(parseResult.getErrorRows())
                .setSuccessRows(parseResult.getSuccessRows());

        if (parseResult.getEntities().isEmpty()) {
            throw apiExceptionFactory.badRequestException("operation.user.import.empty.file");
        }

        DataLogEntity dataLog = new DataLogEntity();
        dataLog.setJobType(DataOperationsEnum.IMPORT);
        dataLog.setEntityType(UserEntity.class.getSimpleName());
        dataLog.setStatus(DataOperationStatusEnum.STARTED);
        dataLog.setStartedAt(Instant.now());
        dataLog.setTriggeredBy(securityContextService.getUser());
        dataLog.setJobId(UUID.fromString(parseResult.getParseId()));

        dataLogRepository.save(dataLog);

        Set<Integer> rolesId = parseResult.getEntities().stream()
                .map(UserImportDto::getRoleId)
                .collect(Collectors.toSet());

        Map<Integer, RoleEntity> rolesMap = roleRepository.findAllById(rolesId).stream()
                .collect(Collectors.toMap(RoleEntity::getId, role -> role));

        List<UserEntity> usersToSave = new ArrayList<>();
        int rowNum = 1;
        for (UserImportDto userImport : parseResult.getEntities()) {
            RoleEntity role = rolesMap.get(userImport.getRoleId());

            if (role == null) {
                finalResult.getErrors().add("Row " + rowNum + ": Role not found");
                finalResult.setErrorRows(finalResult.getErrorRows() + 1);
                finalResult.setSuccessRows(finalResult.getSuccessRows() - 1);
                rowNum++;
                continue;
            }
            UserEntity user = userMapper.toEntityImport(userImport);
            user.setRole(role);
            usersToSave.add(user);
            rowNum++;
        }

        BatchSaveResultDto saveResult = batchSaveService.saveBatch(usersToSave, userRepository);

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
            apiExceptionFactory.businessException("operation.user.import.invalid.log.details", e.getMessage());
        }

        dataLog.setFinishedAt(Instant.now());
        dataLogRepository.save(dataLog);

        return ApiSuccessDto.of(HttpStatus.OK.value(), messageUtils.getMessage("operation.user.import.success"),
                finalResult);
    }

    @Async
    @Override
    public CompletableFuture<ApiSuccessDto<ImportResultDto>> importUsersAsync(InputStream inputStream) {
        return CompletableFuture.completedFuture(importUsers(inputStream));
    }
}