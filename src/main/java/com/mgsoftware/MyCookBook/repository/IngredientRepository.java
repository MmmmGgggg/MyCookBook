package com.mgsoftware.MyCookBook.repository;

import com.mgsoftware.MyCookBook.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    Ingredient findByName(String name);

}
