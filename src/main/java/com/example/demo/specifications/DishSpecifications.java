package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.DishEntity;

import jakarta.persistence.criteria.Join;

public class DishSpecifications {

    public static Specification<DishEntity> nameContains(String search) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<DishEntity> descriptionContains(String search) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<DishEntity> categoryEquals(String category) {
        return (root, query, cb) -> {
            Join<DishEntity, CategoryEntity> join = root.join("category");
            return cb.equal(cb.lower(join.get("name")), category.toLowerCase());
        };
    }

    public static Specification<DishEntity> isActiveEquals(String status) {
        return (root, query, cb) -> {
            boolean isActive = "active".equalsIgnoreCase(status);
            return cb.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<DishEntity> isFeaturedEquals(String featured) {
        return (root, query, cb) -> {
            boolean isFeatured = "featured".equalsIgnoreCase(featured);
            return cb.equal(root.get("isFeatured"), isFeatured);
        };
    }

    public static Specification<DishEntity> priceGreaterThanOrEqual(Double minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
    }

    public static Specification<DishEntity> priceLessThanOrEqual(Double maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
    }
}