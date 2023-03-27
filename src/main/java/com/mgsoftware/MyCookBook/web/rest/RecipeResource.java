package com.mgsoftware.MyCookBook.web.rest;

import com.mgsoftware.MyCookBook.MockDB;
import com.mgsoftware.MyCookBook.domain.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class RecipeResource {

    private final MockDB mockDB;

    public RecipeResource(MockDB mockDB) {

        this.mockDB = mockDB;
    }
    @PostMapping("/recipes")
    ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe){
        ResponseEntity<Recipe> responseEntity = new ResponseEntity(mockDB.addRecipe(recipe), HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("/recipes/{id}")
    ResponseEntity<Recipe> updateRecipe(@PathVariable UUID id, @RequestBody Recipe recipe){
        ResponseEntity<Recipe> responseEntity = new ResponseEntity(mockDB.updateRecipe(id,recipe), HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/recipes/{id}")
    ResponseEntity<Recipe> getRecipeById(@PathVariable UUID id) {
        if (mockDB.getRecipeById(id) != null) {
            Recipe recipe = mockDB.getRecipeById(id);
            ResponseEntity<Recipe> responseEntity = new ResponseEntity(recipe, HttpStatus.OK);
            return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest", "1").body(recipe);
        }
        else return new ResponseEntity<Recipe>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/recipes")
    ResponseEntity<List<Recipe>> getAll(){
        List<Recipe> recipeList = mockDB.getAllRecipes();
        return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest","1").body(recipeList);
    }

    @DeleteMapping("/recipes/{id}")
    ResponseEntity<?> deleteRecipeById(@PathVariable UUID id) {
        if (mockDB.getRecipeById(id) != null) {
            mockDB.deleteRecipe(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

/*    @GetMapping("/recipes")
    ResponseEntity<Recipe> test(){
        Recipe recipe = new Recipe();
        recipe.setName("Pileca juha");
        recipe.setDescription("Recept za pilecu juhu");
        ResponseEntity<Recipe> responseEntity = new ResponseEntity(recipe, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).header("MyCookBookTest","1").body(recipe);
    }*/

}
