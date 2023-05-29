package com.mgsoftware.MyCookBook.service;

import com.mgsoftware.MyCookBook.domain.Recipe;
import com.mgsoftware.MyCookBook.domain.RecipeIngredient;
import com.mgsoftware.MyCookBook.repository.IngredientRepository;
import com.mgsoftware.MyCookBook.repository.RecipeIngredientRepository;
import com.mgsoftware.MyCookBook.repository.RecipeRepository;
import com.mgsoftware.MyCookBook.repository.UnitRepository;
import com.mgsoftware.MyCookBook.service.dto.RecipeIngredientDTO;
import com.mgsoftware.MyCookBook.service.dto.RecipeWithDetailsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;

    private final IngredientRepository ingredientRepository;

    private final UnitRepository unitRepository;

    public RecipeService(RecipeRepository recipeRepository, RecipeIngredientRepository recipeIngredientRepository, IngredientRepository ingredientRepository, UnitRepository unitRepository) {

        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
    }

    public RecipeWithDetailsDTO getRecipeWithDetails(Recipe recipe) {
        return new RecipeWithDetailsDTO(recipe);
    }

    public Recipe createNewRecipe(RecipeWithDetailsDTO recipeWithDetailsDTO) {

        Recipe recipe = new Recipe();
        recipe.setName(recipeWithDetailsDTO.getName());
        recipe.setDescription(recipeWithDetailsDTO.getDescription());
        Recipe result = recipeRepository.save(recipe);

        Set<RecipeIngredient> recipeIngredientList = new HashSet<>();

        List<RecipeIngredientDTO> recipeIngredientsDTO = recipeWithDetailsDTO.getRecipeIngredientsDTO();
        recipeIngredientsDTO.forEach(recipeIngredientDTO -> {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setIngredient(ingredientRepository.findByName(recipeIngredientDTO.getIngredient()));
            recipeIngredient.setUnit(unitRepository.findByName(recipeIngredientDTO.getUnit()));
            recipeIngredient.setQuantity(recipeIngredientDTO.getQuantity());
            recipeIngredient.setRecipe(recipe);
            recipeIngredientList.add(recipeIngredientRepository.save(recipeIngredient));
        });

        return result;
    }
}


