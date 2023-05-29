package com.mgsoftware.MyCookBook.repository;

import com.mgsoftware.MyCookBook.domain.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, UUID> {


}
