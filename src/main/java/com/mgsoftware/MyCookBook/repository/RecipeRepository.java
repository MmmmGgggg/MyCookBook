package com.mgsoftware.MyCookBook.repository;

import com.mgsoftware.MyCookBook.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
}
