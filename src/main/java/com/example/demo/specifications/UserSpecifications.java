package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.UserEntity;

public class UserSpecifications {
    public static Specification<UserEntity> nameOrEmailContains(String search) {
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("username")), "%" + search.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%"));
    }

    public static Specification<UserEntity> activeEquals(String status) {
        return (root, query, cb) -> {
            boolean isActive = "active".equalsIgnoreCase(status);
            return cb.equal(root.get("active"), isActive);
        };
    }

    public static Specification<UserEntity> roleEquals(String roleName) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("role").get("name")), roleName.toLowerCase());
    }

}
