package com.mgsoftware.MyCookBook.service;

import com.mgsoftware.MyCookBook.domain.*;
import com.mgsoftware.MyCookBook.repository.*;
import com.mgsoftware.MyCookBook.service.dto.RecipeIngredientDTO;
import com.mgsoftware.MyCookBook.service.dto.RecipeWithDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@EnableScheduling
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;

    private final IngredientRepository ingredientRepository;

    private final UnitRepository unitRepository;

    private final RecipeSearchRepository recipeSearchRepository;

    public RecipeService(RecipeRepository recipeRepository, RecipeIngredientRepository recipeIngredientRepository, IngredientRepository ingredientRepository, UnitRepository unitRepository, RecipeSearchRepository recipeSearchRepository) {

        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
        this.recipeSearchRepository = recipeSearchRepository;
    }

    public RecipeWithDetailsDTO getRecipeWithDetails(Recipe recipe) {
        return new RecipeWithDetailsDTO(recipe);
    }

    public Recipe createNewRecipe(RecipeWithDetailsDTO recipeWithDetailsDTO) {

        Recipe recipe = new Recipe();
        recipe.setName(recipeWithDetailsDTO.getName());
        recipe.setDescription(recipeWithDetailsDTO.getDescription());
        recipe.setProcessed(false);
        Recipe result = recipeRepository.save(recipe);

        Set<RecipeIngredient> recipeIngredientList = new HashSet<>();

        List<RecipeIngredientDTO> recipeIngredientsDTO = recipeWithDetailsDTO.getRecipeIngredientsDTO();
        recipeIngredientsDTO.forEach(recipeIngredientDTO -> {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            Ingredient ingredient = ingredientRepository.findByName(recipeIngredientDTO.getIngredient());
            if (ingredient == null)  {
                ingredient = new Ingredient();
                ingredient.setName(recipeIngredientDTO.getIngredient());
                ingredientRepository.save(ingredient);
            }
            recipeIngredient.setIngredient(ingredient);
            Unit unit = unitRepository.findByName(recipeIngredientDTO.getUnit());
            if (unit == null)  {
                unit = new Unit();
                unit.setName(recipeIngredientDTO.getUnit());
                unitRepository.save(unit);
            }
            recipeIngredient.setUnit(unit);
            recipeIngredient.setQuantity(recipeIngredientDTO.getQuantity());
            recipeIngredient.setRecipe(recipe);
            recipeIngredientList.add(recipeIngredientRepository.save(recipeIngredient));
        });

        return result;
    }

    public Page<Recipe> getRecipeBySearch(String ingredients, Integer pageNo, Integer pageSize) {
        ingredients = ingredients + ",";
        String[] searchIngredients = (ingredients).split("((?<=,))");
        Arrays.sort(searchIngredients);
        List<String> inputStrings = Arrays.asList(searchIngredients);
        List<String> result = getAllCombinations(inputStrings);
        Pageable paging = PageRequest.of(pageNo, pageSize);
        final Page<Recipe> allBySearch = recipeRepository.findAllBySearch(
                result,paging);
/*       final List<Recipe> allBySearch = recipeRepository.findAllBySearch(
                List.of( "Luk,Mrkva,Piletina,","Luk,Mrkva,","Luk,Piletina,","Luk,","Mrkva,Piletina,","Mrkva,","Piletina,"));*/
        return allBySearch;

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
         final Set <Unit> units = new HashSet<>();

         for (String un : existingUnits) {
             Unit unit = unitRepository.findByName(un);
             if (unit == null) {
                 unit = new Unit();
                 unit.setName(un);
                 unitRepository.save(unit);
             }
             units.add(unit);
         }
         //final Set<Unit> units = unitRepository.findAllByNameIn(existingUnits);

         final List<String> existingIngredients = forAdding.stream().map(it -> newRecipeIngredients.get(it).getIngredient())
                 .collect(Collectors.toList());
        final Set<Ingredient> ingredients =  new HashSet<>();
        for (String ingr : existingIngredients) {
            Ingredient ingredient = ingredientRepository.findByName(ingr);
            if (ingredient == null)  {
                ingredient = new Ingredient();
                ingredient.setName(ingr);
                ingredientRepository.save(ingredient);
            }
            ingredients.add(ingredient);
        }
         //final Set<Ingredient> ingredients = ingredientRepository.findAllByNameIn(
         //        existingIngredients);
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

    @Scheduled(fixedDelay = 60000)
    public void searchIngredientsJob(){

        for(Recipe recipe : recipeRepository.findAllByProcessed(false)) {

            Set<RecipeIngredient> ingredientsForRecipe= recipe.getRecipeIngredients();
            Iterator<RecipeIngredient> it = ingredientsForRecipe.iterator();
            String[] ingredients = new String[ingredientsForRecipe.size()];
            int k=0;
            while (it.hasNext()) {
                String ingredient = it.next().getIngredient().getName();
                ingredients[k] = ingredient;
                k++;
            }
            Arrays.sort(ingredients);
            String allIngredientscombination="";
            for (String ingredient : ingredients) {
                allIngredientscombination +=ingredient + "," ;
            }
            RecipeSearch recipeSearch = new RecipeSearch();
            recipeSearch.setRecipe(recipe);
            recipeSearch.setIngredientsCombination(allIngredientscombination);
            recipeSearch.setNrCombinations(ingredients.length);
            recipeSearchRepository.save(recipeSearch);

            int nrOfComb = ingredients.length -1;
            while(nrOfComb>0) {
                String tempCombinations[] = new String[nrOfComb];
                // Save all combination using temporary array 'tempCombinations[]'
                saveCombinations(ingredients, tempCombinations, 0, ingredients.length - 1, 0, nrOfComb, recipe);
                nrOfComb--;
            }
            System.out.println(
                    "Found not processed recipe - " + recipe.getName());
            recipe.setProcessed(true);
        }

    }


    /* allCombinations[]  ---> Input Array
   tempCombinations[] ---> Temporary array to store current combination
   start & end ---> Starting and Ending indexes in allCombinations[]
   index  ---> Current index in tempCombinations[]
   r ---> Size of a combination to be printed */
    void saveCombinations(String allCombinations[], String tempCombinations[], int start,
                          int end, int index, int r, Recipe recipe)
    {
        // Current combination is ready to be saved
        if (index == r)
        {
            String ingredientscombination="";
            for (int j=0; j<r; j++) {
                System.out.print(tempCombinations[j] + " ");
                ingredientscombination +=tempCombinations[j] + ",";
            }
            RecipeSearch recipeSearch = new RecipeSearch();
            recipeSearch.setRecipe(recipe);
            recipeSearch.setIngredientsCombination(ingredientscombination);
            recipeSearch.setNrCombinations(tempCombinations.length);
            recipeSearchRepository.save(recipeSearch);
            return ;
        }
        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            tempCombinations[index] = allCombinations[i];
            saveCombinations(allCombinations, tempCombinations, i+1, end, index+1, r, recipe);
        }
    }



    public static List<String> getAllCombinations(List<String> inputStrings) {
        List<String> combinations = new ArrayList<>();
        generateCombinations(inputStrings, 0, "", combinations);
        return combinations;
    }

    private static void generateCombinations(List<String> inputStrings, int index, String currentCombination, List<String> combinations) {
        if (index == inputStrings.size()) {
            if (!currentCombination.isEmpty())
                combinations.add(currentCombination);
            return;
        }

        // Include the current string in the combination
        generateCombinations(inputStrings, index + 1, currentCombination + inputStrings.get(index), combinations);

        // Exclude the current string from the combination
        generateCombinations(inputStrings, index + 1, currentCombination, combinations);
    }
}


