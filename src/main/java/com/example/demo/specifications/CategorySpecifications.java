package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.CategoryEntity;

public class CategorySpecifications {
    public static Specification<CategoryEntity> categoryTypeEquals(String categoryType) {
        return (root, query, cb) -> {
            return cb.equal(root.get("categoryType"), categoryType);
        };
    }
}
