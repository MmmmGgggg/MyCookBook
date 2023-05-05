package com.mgsoftware.MyCookBook.web.rest;

import com.mgsoftware.MyCookBook.domain.Recipe;
import com.mgsoftware.MyCookBook.repository.RecipeRepository;
import com.mgsoftware.MyCookBook.service.dto.RecipeWithDetailsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class RecipeResource {


    private final RecipeRepository recipeRepository;

    public RecipeResource(RecipeRepository recipeRepository) {

        this.recipeRepository = recipeRepository;
    }
    @PostMapping("/recipes")
    ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe){
        ResponseEntity<Recipe> responseEntity = new ResponseEntity(recipeRepository.save(recipe), HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("/recipes/{id}")
    ResponseEntity<Recipe> updateRecipe(@PathVariable UUID id, @RequestBody Recipe recipe) {
        if (recipeRepository.findById(id) != null) {
            Recipe existingRecipe = recipeRepository.getReferenceById(id);
            existingRecipe.setName(recipe.getName());
            existingRecipe.setDescription(recipe.getDescription());
            ResponseEntity<Recipe> responseEntity = new ResponseEntity(recipeRepository.save(existingRecipe), HttpStatus.OK);
            return responseEntity;
        } else
            return new ResponseEntity<Recipe>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recipes/{id}")
    ResponseEntity<Recipe> getRecipeById(@PathVariable UUID id) {
        if (recipeRepository.findById(id) != null) {
            Recipe recipe = recipeRepository.getReferenceById(id);
            //RecipeWithDetailsDTO recipeWithDetailsDTO = new RecipeWithDetailsDTO(recipe);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest", "1").body(recipe);
        }
        else return new ResponseEntity<Recipe>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recipes/{id}/details")
    ResponseEntity<RecipeWithDetailsDTO> getRecipeWithDetails(@PathVariable UUID id) {
        if (recipeRepository.findById(id) != null) {
            Recipe recipe = recipeRepository.getReferenceById(id);
            RecipeWithDetailsDTO recipeWithDetailsDTO = new RecipeWithDetailsDTO(recipe);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest", "1").body(recipeWithDetailsDTO);
        }
        else return new ResponseEntity<RecipeWithDetailsDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recipes")
    ResponseEntity<List<Recipe>> getAll(){
        List<Recipe> recipeList = recipeRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest","1").body(recipeList);
    }

    @DeleteMapping("/recipes/{id}")
    ResponseEntity<?> deleteRecipeById(@PathVariable UUID id) {
        if (recipeRepository.findById(id) != null) {
            recipeRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
