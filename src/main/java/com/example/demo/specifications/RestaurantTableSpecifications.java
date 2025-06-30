package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.RestaurantTableEntity;

public class RestaurantTableSpecifications {

    public static Specification<RestaurantTableEntity> numberEquals(String number) {
        return (root, query, cb) -> cb.equal(root.get("number"), number);
    }

    public static Specification<RestaurantTableEntity> statusEquals(String status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<RestaurantTableEntity> sectionEquals(String section) {
        return (root, query, cb) -> cb.equal(root.get("section"), section);
    }
}
