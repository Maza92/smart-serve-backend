package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.InventoryItemEntity;

public class InventoryItemSpecifications {

    public static Specification<InventoryItemEntity> nameContains(String search) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<InventoryItemEntity> locationEquals(String location) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("location")), location.toLowerCase());
    }

    public static Specification<InventoryItemEntity> isActiveEquals(String status) {
        return (root, query, cb) -> {
            boolean isActive = "active".equalsIgnoreCase(status);
            return cb.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<InventoryItemEntity> supplierNameContains(String supplierName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("supplier").get("name")),
                "%" + supplierName.toLowerCase() + "%");
    }
}