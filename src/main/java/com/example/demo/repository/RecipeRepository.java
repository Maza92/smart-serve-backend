package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RecipeEntity;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {

    @EntityGraph(attributePaths = { "inventoryItem" })
    @Query("SELECT r FROM RecipeEntity r WHERE r.dish.id = :id")
    List<RecipeEntity> findAllByDishWithIngredients(Integer id);
}