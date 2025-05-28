package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.SupplierEntity;

public class SupplierSpecifications {

    public static Specification<SupplierEntity> nameContains(String search) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<SupplierEntity> contactPersonContains(String search) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("contactPerson")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<SupplierEntity> emailContains(String search) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<SupplierEntity> isActiveEquals(String status) {
        return (root, query, cb) -> {
            boolean isActive = "active".equalsIgnoreCase(status);
            return cb.equal(root.get("isActive"), isActive);
        };
    }
}