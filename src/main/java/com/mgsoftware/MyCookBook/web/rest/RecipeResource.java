package com.mgsoftware.MyCookBook.web.rest;

import com.mgsoftware.MyCookBook.domain.Recipe;
import com.mgsoftware.MyCookBook.repository.RecipeRepository;
import com.mgsoftware.MyCookBook.service.RecipeService;
import com.mgsoftware.MyCookBook.service.dto.RecipeWithDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

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

    @PutMapping("/recipes/{id}")
    ResponseEntity<RecipeWithDetailsDTO> updateRecipe(@PathVariable UUID id, @RequestBody RecipeWithDetailsDTO recipeWithDetailsDTO) {
        if (recipeRepository.findById(id) != null) {
            Recipe recipe = recipeService.updateRecipe(recipeWithDetailsDTO, id);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest", "1").body(recipeWithDetailsDTO);
        } else
            return new ResponseEntity<RecipeWithDetailsDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recipes/{id}")
    ResponseEntity<RecipeWithDetailsDTO> getRecipeById(@PathVariable UUID id) {
        if (recipeRepository.findById(id) != null) {
            Recipe recipe = recipeRepository.getReferenceById(id);
            RecipeWithDetailsDTO recipeWithDetailsDTO = recipeService.getRecipeWithDetails(recipe);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest", "1").body(recipeWithDetailsDTO);
        }
        else return new ResponseEntity<RecipeWithDetailsDTO>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recipes")
    ResponseEntity<Page<Recipe>> getRecipeBySearch(@RequestParam(defaultValue = "0") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false) String ingredientsCombination){
        if (ingredientsCombination!=null) {
            final Page<Recipe> recipeList = recipeService.getRecipeBySearch(ingredientsCombination, pageNo, pageSize);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest", "1").body(recipeList);
        }
        else {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            final Page<Recipe> recipeList = recipeRepository.findAll(paging);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest","1").body(recipeList);
        }
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
