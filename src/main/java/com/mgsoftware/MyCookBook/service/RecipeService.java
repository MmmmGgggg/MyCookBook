package com.mgsoftware.MyCookBook.service;

import com.mgsoftware.MyCookBook.domain.Ingredient;
import com.mgsoftware.MyCookBook.domain.Recipe;
import com.mgsoftware.MyCookBook.domain.RecipeIngredient;
import com.mgsoftware.MyCookBook.domain.Unit;
import com.mgsoftware.MyCookBook.repository.IngredientRepository;
import com.mgsoftware.MyCookBook.repository.RecipeIngredientRepository;
import com.mgsoftware.MyCookBook.repository.RecipeRepository;
import com.mgsoftware.MyCookBook.repository.UnitRepository;
import com.mgsoftware.MyCookBook.service.dto.RecipeIngredientDTO;
import com.mgsoftware.MyCookBook.service.dto.RecipeWithDetailsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
     public Recipe updateRecipe(RecipeWithDetailsDTO recipeWithDetailsDTO, UUID id) {
         Recipe existingRecipe = recipeRepository.getReferenceById(id);
         existingRecipe.setName(recipeWithDetailsDTO.getName());
         existingRecipe.setDescription(recipeWithDetailsDTO.getDescription());

         final HashMap<String, RecipeIngredient> existingRecipeIngredients = new HashMap<>();
         for (RecipeIngredient it: existingRecipe.getRecipeIngredients()) {
             existingRecipeIngredients.put(it.getIngredient().getName(), it);
         }

         final HashMap<String, RecipeIngredientDTO> newRecipeIngredients = new HashMap<>();
         for (RecipeIngredientDTO recipeIngredientDTO: recipeWithDetailsDTO.getRecipeIngredientsDTO()) {
             newRecipeIngredients.put(recipeIngredientDTO.getIngredient(), recipeIngredientDTO);
         }

         final Set<String> existingRecipeIngredientsKeys = existingRecipeIngredients.keySet();
         final Set<String> newRecipeIngredientsKeys = newRecipeIngredients.keySet();

         final Set<String> forDeleting = existingRecipeIngredientsKeys.stream()
                 .filter(it -> !newRecipeIngredientsKeys.contains(it)).collect(Collectors.toSet());

         final Set<String> forAdding = newRecipeIngredientsKeys.stream().filter(it -> !existingRecipeIngredientsKeys.contains(it))
                 .collect(Collectors.toSet());

         final Set<String> forUpdating = existingRecipeIngredientsKeys.stream().filter(newRecipeIngredientsKeys::contains)
                 .collect(Collectors.toSet());

         updateExisting(existingRecipeIngredients, newRecipeIngredients, forUpdating);

         for (String name: forDeleting) {
             existingRecipe.removeRecipeIngredient(existingRecipeIngredients.get(name));
         }

         final List<String> existingUnits = forAdding.stream().map(it -> newRecipeIngredients.get(it).getUnit())
                 .collect(Collectors.toList());
         final Set<Unit> units = unitRepository.findAllByNameIn(existingUnits);

         final List<String> existingIngredients = forAdding.stream().map(it -> newRecipeIngredients.get(it).getIngredient())
                 .collect(Collectors.toList());
         final Set<Ingredient> ingredients = ingredientRepository.findAllByNameIn(
                 existingIngredients);
         for (String name: forAdding) {
             final RecipeIngredientDTO recipeIngredientDTO = newRecipeIngredients.get(name);

             final RecipeIngredient recipeIngredient = new RecipeIngredient();
             recipeIngredient.setUnit(
                     units.stream()
                             .filter(it -> it.getName().equals(recipeIngredientDTO.getUnit()))
                             .findFirst().get());
             recipeIngredient.setIngredient(
                     ingredients.stream()
                             .filter(it -> it.getName().equals(recipeIngredientDTO.getIngredient()))
                             .findFirst().get());
             recipeIngredient.setQuantity(recipeIngredientDTO.getQuantity());

             existingRecipe.addRecipeIngredient(recipeIngredient);
         }

         return recipeRepository.save(existingRecipe);

    }

    private void updateExisting(HashMap<String, RecipeIngredient> existingIngredientForRecipe,
                                HashMap<String, RecipeIngredientDTO> newIngredientForRecipe, Set<String> forUpdating) {
        final List<String> existingUnits = forUpdating.stream().map(it -> newIngredientForRecipe.get(it).getUnit())
                .collect(Collectors.toList());

        final Set<Unit> units = unitRepository.findAllByNameIn(existingUnits);

        for (String ingredientName : forUpdating) {
            final RecipeIngredient recipeIngredient = existingIngredientForRecipe.get(ingredientName);
            final RecipeIngredientDTO recipeIngredientDTO = newIngredientForRecipe.get(ingredientName);

            recipeIngredient.setQuantity(recipeIngredientDTO.getQuantity());
            recipeIngredient.setUnit(units.stream()
                    .filter(it -> it.getName().equals(recipeIngredientDTO.getUnit())).findFirst().get());
        }
    }

}


