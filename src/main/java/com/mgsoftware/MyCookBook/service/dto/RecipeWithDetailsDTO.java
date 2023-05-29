package com.mgsoftware.MyCookBook.service.dto;

import com.mgsoftware.MyCookBook.domain.Recipe;
import com.mgsoftware.MyCookBook.domain.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecipeWithDetailsDTO {

    String name;
    String description;
    List<RecipeIngredientDTO> recipeIngredientsDTO =  new ArrayList<>();

    public RecipeWithDetailsDTO(){

    }

    public RecipeWithDetailsDTO(Recipe recipe) {
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        Set<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            recipeIngredientsDTO.add(new RecipeIngredientDTO(recipeIngredient.getIngredient().getName(), recipeIngredient.getQuantity(), recipeIngredient.getUnit().getName()));
        }
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public List<RecipeIngredientDTO> getRecipeIngredientsDTO() {
        return recipeIngredientsDTO;
    }
}
