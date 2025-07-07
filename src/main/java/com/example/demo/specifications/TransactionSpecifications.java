package com.example.demo.specifications;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.TransactionEntity;
import com.example.demo.enums.PaymentMethodEnum;
import com.example.demo.enums.TransactionStatusEnum;
import com.example.demo.enums.TransactionTypeEnum;

public class TransactionSpecifications {

    public static Specification<TransactionEntity> cashRegisterIdEquals(Integer cashRegisterId) {
        return (root, query, criteriaBuilder) -> {
            if (cashRegisterId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("cashRegister").get("id"), cashRegisterId);
        };
    }

    public static Specification<TransactionEntity> orderIdEquals(Integer orderId) {
        return (root, query, criteriaBuilder) -> {
            if (orderId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("order").get("id"), orderId);
        };
    }

    public static Specification<TransactionEntity> userIdEquals(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<TransactionEntity> paymentMethodEquals(PaymentMethodEnum paymentMethod) {
        return (root, query, criteriaBuilder) -> {
            if (paymentMethod == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod);
        };
    }

    public static Specification<TransactionEntity> transactionTypeEquals(TransactionTypeEnum transactionType) {
        return (root, query, criteriaBuilder) -> {
            if (transactionType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("transactionType"), transactionType);
        };
    }

    public static Specification<TransactionEntity> statusEquals(TransactionStatusEnum status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<TransactionEntity> dateRangeBetween(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), endDate);
            }
            if (endDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), startDate);
            }
            return criteriaBuilder.between(root.get("transactionDate"), startDate, endDate);
        };
    }

    public static Specification<TransactionEntity> activeEquals(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }
}