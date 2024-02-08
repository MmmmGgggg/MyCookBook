package com.mgsoftware.MyCookBook.repository;

import com.mgsoftware.MyCookBook.domain.RecipeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeSearchRepository extends JpaRepository<RecipeSearch, UUID> {

    List<RecipeSearch> findAllByIngredientsCombinationIn(List<String> ingredientsCombination);

}
