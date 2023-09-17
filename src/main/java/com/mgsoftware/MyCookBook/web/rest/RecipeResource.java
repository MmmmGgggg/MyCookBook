package com.mgsoftware.MyCookBook.web.rest;

import com.mgsoftware.MyCookBook.domain.Recipe;
import com.mgsoftware.MyCookBook.repository.RecipeRepository;
import com.mgsoftware.MyCookBook.service.RecipeService;
import com.mgsoftware.MyCookBook.service.dto.RecipeWithDetailsDTO;
import org.apache.tomcat.util.http.HeaderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@RestController
public class RecipeResource {


    private final RecipeRepository recipeRepository;

    private final RecipeService recipeService;

    public RecipeResource(RecipeRepository recipeRepository, RecipeService recipeService) {

        this.recipeRepository = recipeRepository;
        this.recipeService = recipeService;
    }
    @PostMapping("/recipes")
    public ResponseEntity<Recipe> createRecipe( @RequestBody RecipeWithDetailsDTO recipeWithDetailsDTO) throws URISyntaxException {
        Recipe result = recipeService.createNewRecipe(recipeWithDetailsDTO);
        return ResponseEntity
                .created(new URI("/api/recipes/" + result.getId()))
                .header("MyCookBookTest", "1").body(result);
    }

/*
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
*/

    @PutMapping("/recipes/{id}")
    ResponseEntity<RecipeWithDetailsDTO> updateRecipe(@PathVariable UUID id, @RequestBody RecipeWithDetailsDTO recipeWithDetailsDTO) {
        if (recipeRepository.findById(id) != null) {
            Recipe recipe = recipeService.updateRecipe(recipeWithDetailsDTO, id);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest", "1").body(recipeWithDetailsDTO);
        } else
            return new ResponseEntity<RecipeWithDetailsDTO>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/recipes/{id}")
    ResponseEntity<Recipe> getRecipeById(@PathVariable UUID id) {
        if (recipeRepository.findById(id) != null) {
            Recipe recipe = recipeRepository.getReferenceById(id);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest", "1").body(recipe);
        }
        else return new ResponseEntity<Recipe>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recipes/{id}/details")
    ResponseEntity<RecipeWithDetailsDTO> getRecipeWithDetails(@PathVariable UUID id) {
        if (recipeRepository.findById(id) != null) {
            Recipe recipe = recipeRepository.getReferenceById(id);
            RecipeWithDetailsDTO recipeWithDetailsDTO = recipeService.getRecipeWithDetails(recipe);
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
