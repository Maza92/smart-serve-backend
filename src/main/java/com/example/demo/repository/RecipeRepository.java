package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RecipeEntity;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {

    @EntityGraph(attributePaths = { "inventoryItem", "unit" })
    @Query("SELECT r FROM RecipeEntity r WHERE r.id = :id")
    Optional<RecipeEntity> findByIdWithItemAndUnit(@Param("id") Integer id);

    @EntityGraph(attributePaths = { "inventoryItem", "dish" })
    @Query("SELECT r FROM RecipeEntity r WHERE r.dish.id = :id")
    Optional<RecipeEntity> findByDishId(Integer id);

    @EntityGraph(attributePaths = { "inventoryItem" })
    @Query("SELECT r FROM RecipeEntity r WHERE r.dish.id = :id")
    List<RecipeEntity> findAllByDishWithIngredients(Integer id);

    @EntityGraph(attributePaths = { "inventoryItem", "unit" })
    @Query("SELECT r FROM RecipeEntity r WHERE r.dish.id = :id")
    List<RecipeEntity> findAllByDishId(Integer id);
}