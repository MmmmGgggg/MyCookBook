package com.mgsoftware.MyCookBook.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgsoftware.MyCookBook.domain.Ingredient;
import com.mgsoftware.MyCookBook.domain.Recipe;
import com.mgsoftware.MyCookBook.domain.RecipeIngredient;
import com.mgsoftware.MyCookBook.domain.Unit;
import com.mgsoftware.MyCookBook.repository.IngredientRepository;
import com.mgsoftware.MyCookBook.repository.RecipeIngredientRepository;
import com.mgsoftware.MyCookBook.repository.RecipeRepository;
import com.mgsoftware.MyCookBook.repository.UnitRepository;
import com.mgsoftware.MyCookBook.service.dto.RecipeWithDetailsDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CreateRecipeTest {

    private static final String DEFAULT_NAME = "TestRecipe";
    private static final String UPDATED_NAME = "UpdatedTestRecipe";
    private static final String DEFAULT_DESCRIPTION = "TestRecipe_description";
    private static final String UPDATED_DESCRIPTION = "UpdatedTestRecipe_description";

    private static final String ENTITY_API_URL = "/recipes";

    @Autowired
    private MockMvc restRecipeMockMvc;
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    ObjectMapper objectMapper;
    private Recipe recipe;

    @Autowired
    private RecipeResource recipeResource;


    public Recipe createEntity(EntityManager em) {
        Recipe recipe = new Recipe();
        recipe.setName(DEFAULT_NAME);
        recipe.setDescription(DEFAULT_DESCRIPTION);

        Set<RecipeIngredient> recipeIngredientList = new HashSet<>();

        Ingredient ingredient_1 = new Ingredient();
        ingredient_1.setName("Mrkva");
        ingredientRepository.saveAndFlush(ingredient_1);

        Ingredient ingredient_2 = new Ingredient();
        ingredient_2.setName("Luk");
        ingredientRepository.saveAndFlush(ingredient_2);

        Unit unit_1 = new Unit();
        unit_1.setName("gram");
        unitRepository.saveAndFlush(unit_1);

        RecipeIngredient recipeIngredient_1 = new RecipeIngredient();
        recipeIngredient_1.setIngredient(ingredientRepository.findByName("Mrkva"));
        recipeIngredient_1.setUnit(unitRepository.findByName("gram"));
        recipeIngredient_1.setQuantity(100.0);
        recipeIngredient_1.setRecipe(recipe);
        recipeIngredientList.add(recipeIngredient_1);

        RecipeIngredient recipeIngredient_2 = new RecipeIngredient();
        recipeIngredient_2.setIngredient(ingredientRepository.findByName("Luk"));
        recipeIngredient_2.setUnit(unitRepository.findByName("gram"));
        recipeIngredient_2.setQuantity(100.0);
        recipeIngredient_2.setRecipe(recipe);
        recipeIngredientList.add(recipeIngredient_2);
        recipe.addRecipeIngredient(recipeIngredient_1);
        recipe.addRecipeIngredient(recipeIngredient_2);
        recipe.setRecipeIngredients(recipeIngredientList);
        //recipeRepository.saveAndFlush(recipe);
        return recipe;

    }

    @BeforeEach
    public void initTest() {
        recipe = createEntity(em);

    }

    @Test
    public void createRecipe() throws Exception {

        int databaseSizeBeforeCreate = recipeRepository.findAll().size();

        RecipeWithDetailsDTO recipeWithDetailsDTO = new RecipeWithDetailsDTO(recipe);

        restRecipeMockMvc.perform(post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeWithDetailsDTO)))
                .andExpect(status().isCreated());
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate + 1);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecipe.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRecipe.getRecipeIngredients()).hasSize(2);

    }
}
