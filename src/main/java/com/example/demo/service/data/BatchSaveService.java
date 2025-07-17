package com.example.demo.service.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.data.BatchSaveResultDto;
import com.example.demo.utils.MessageUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatchSaveService {
    private static final Integer BATCH_SIZE = 1000;
    private final MessageUtils messageUtils;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public <T> BatchSaveResultDto saveBatch(List<T> entities, JpaRepository<T, ?> repository) {
        BatchSaveResultDto result = new BatchSaveResultDto();
        int batchCount = 0;
        int totalSaved = 0;

        for (int i = 0; i < entities.size(); i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, entities.size());
            List<T> batch = entities.subList(i, endIndex);

            try {
                repository.saveAll(batch);
                entityManager.flush();
                entityManager.clear();
                batchCount++;
                totalSaved += batch.size();
            } catch (Exception e) {
                result.getErrors().add(messageUtils.getMessage("operation.data.save.failed", e.getMessage()));
                result.setErrorCount(result.getErrorCount() + batch.size());
            }
        }

        result.setSuccessCount(totalSaved);
        result.setBatchCount(batchCount);
        return result;
    }

    @Async
    @Transactional
    public <T> CompletableFuture<BatchSaveResultDto> saveBatchAsync(List<T> entities,
            JpaRepository<T, ?> repository) {
        return CompletableFuture.completedFuture(saveBatch(entities, repository));
    }
}
