package com.mgsoftware.MyCookBook;

import com.mgsoftware.MyCookBook.domain.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MockDB {

    private final Logger log = LoggerFactory.getLogger(MockDB.class);

    Map<UUID, Recipe> recipeDB = new HashMap<>();

    MockDB() {
        log.info("DB initalization");
        Recipe recipe1 = new Recipe();
        recipe1.setName("Pileca juha");
        recipe1.setDescription("Recept za pilecu juhu");
        recipe1.setId(UUID.randomUUID());
        Recipe recipe2 = new Recipe();
        recipe2.setName("Spageti Bolonjez");
        recipe2.setDescription("Recept za bolonjez");
        recipe2.setId(UUID.randomUUID());
        recipeDB.put(recipe1.getId(), recipe1);
        recipeDB.put(recipe2.getId(), recipe2);

    }

    public Recipe getRecipeById(UUID id) {
        return recipeDB.get(id);
    }

    public Recipe addRecipe(Recipe recipe) {
        if (recipe.getId() == null) {
            recipe.setId(UUID.randomUUID());
        }
        recipeDB.put(recipe.getId(), recipe);
        return recipe;
    }

    public Recipe updateRecipe(UUID id, Recipe recipe) {
        recipe.setId(id);
        recipeDB.replace(id, recipe);
     return recipe;
    }

    public void deleteRecipe(UUID id) {
        Recipe recipe = recipeDB.get(id);
        recipeDB.remove(id);
    }

    public ArrayList<Recipe> getAllRecipes() {
        return new ArrayList<Recipe>(recipeDB.values());

    }
}
