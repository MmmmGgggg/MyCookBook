package com.mgsoftware.MyCookBook.repository;

import com.mgsoftware.MyCookBook.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    List<Recipe> findAllByProcessed(boolean processed);

    @Query("SELECT r "
            + "FROM Recipe r "
            + "JOIN RecipeSearch rs ON(r.id = rs.recipe.id) "
            + "WHERE rs.ingredientsCombination IN :search "
            + "GROUP BY r.id, r.name, r.description "
            + "ORDER BY MAX(rs.nrCombinations) DESC")
    List<Recipe> findAllBySearch(@Param("search") Collection<String> search);
}
