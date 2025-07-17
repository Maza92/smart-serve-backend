package com.example.demo.specifications;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.CashMovementEntity;
import com.example.demo.enums.CashMovementTypeEnum;

public class CashMovementSpecifications {

    public static Specification<CashMovementEntity> cashRegisterIdEquals(Integer cashRegisterId) {
        return (root, query, criteriaBuilder) -> {
            if (cashRegisterId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("cashRegister").get("id"), cashRegisterId);
        };
    }

    public static Specification<CashMovementEntity> userIdEquals(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<CashMovementEntity> movementTypeEquals(CashMovementTypeEnum movementType) {
        return (root, query, criteriaBuilder) -> {
            if (movementType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("movementType"), movementType);
        };
    }

    public static Specification<CashMovementEntity> dateRangeBetween(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("movementDate"), endDate);
            }
            if (endDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("movementDate"), startDate);
            }
            return criteriaBuilder.between(root.get("movementDate"), startDate, endDate);
        };
    }

    public static Specification<CashMovementEntity> activeEquals(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }
}