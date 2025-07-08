package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.NotificationsEntity;
import com.example.demo.entity.UserEntity;

import jakarta.validation.constraints.NotNull;

public interface NotificationsRepository
        extends JpaRepository<NotificationsEntity, Integer>, JpaSpecificationExecutor<NotificationsEntity> {

    @EntityGraph(attributePaths = { "user" })
    Page<NotificationsEntity> findAll(@NotNull Specification<NotificationsEntity> spec, Pageable pageable);

    @EntityGraph(attributePaths = { "user" })
    @Query("SELECT n FROM NotificationsEntity n WHERE n.id = :id")
    Optional<NotificationsEntity> findByIdWithUser(@NotNull Integer id);

    @Query(value = "SELECT n FROM NotificationsEntity n WHERE n.user = :user AND n.isRead = :isRead ORDER BY n.createdAt DESC", nativeQuery = false)
    List<NotificationsEntity> findTop20ByUserAndIsReadOrderByCreatedAtDesc(UserEntity user, boolean isRead);

    @Query(value = "SELECT n FROM NotificationsEntity n ORDER BY n.createdAt DESC", nativeQuery = false)
    List<NotificationsEntity> findRecentNotifications(Pageable pageable);
}
