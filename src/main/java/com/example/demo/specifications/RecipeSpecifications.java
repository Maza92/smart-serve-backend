package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.RecipeEntity;

public class RecipeSpecifications {
    public static Specification<RecipeEntity> dishIdEquals(Integer dishId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("dish").get("id"), dishId);
        };
    }

    public static Specification<RecipeEntity> inventoryItemIdEquals(Integer inventoryItemId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("inventoryItem").get("id"), inventoryItemId);
        };
    }
}