package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.NotificationsEntity;
import com.example.demo.entity.UserEntity;

public interface NotificationsRepository extends JpaRepository<NotificationsEntity, Integer> {

    @Query(value = "SELECT n FROM NotificationsEntity n WHERE n.user = :user AND n.isRead = :isRead ORDER BY n.createdAt DESC", nativeQuery = false)
    List<NotificationsEntity> findTop20ByUserAndIsReadOrderByCreatedAtDesc(UserEntity user, boolean isRead);

}
