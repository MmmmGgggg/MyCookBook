package com.mgsoftware.MyCookBook.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgsoftware.MyCookBook.domain.Recipe;
import com.mgsoftware.MyCookBook.domain.RecipeIngredient;
import com.mgsoftware.MyCookBook.repository.IngredientRepository;
import com.mgsoftware.MyCookBook.repository.RecipeIngredientRepository;
import com.mgsoftware.MyCookBook.repository.RecipeRepository;
import com.mgsoftware.MyCookBook.repository.UnitRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeResourceTest {

    private static final String DEFAULT_NAME = "TestRecipe";
    private static final String UPDATED_NAME = "UpdatedTestRecipe";
    private static final String DEFAULT_DESCRIPTION = "TestRecipe_description";
    private static final String UPDATED_DESCRIPTION = "UpdatedTestRecipe_description";

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

    private Recipe updatedRecipe;

    @Autowired
    private RecipeResource recipeResource;

    private static final String ENTITY_API_URL = "/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public Recipe createEntity(EntityManager em) {
        Recipe recipe = new Recipe();
        recipe.setName(DEFAULT_NAME);
        recipe.setDescription(DEFAULT_DESCRIPTION);

/*        Set<RecipeIngredient> recipeIngredientList = new HashSet<>();
        RecipeIngredient recipeIngredient_1 = new RecipeIngredient();
        recipeIngredient_1.setIngredient(ingredientRepository.findByName("Mrkva"));
        recipeIngredient_1.setUnit(unitRepository.findByName("gram"));
        recipeIngredient_1.setQuantity(100.0);
        RecipeIngredient recipeIngredient_2 = new RecipeIngredient();
        recipeIngredient_2.setIngredient(ingredientRepository.findByName("Mrkva"));
        recipeIngredient_2.setUnit(unitRepository.findByName("gram"));
        recipeIngredient_2.setQuantity(100.0);
        recipeIngredientList.add(recipeIngredient_1);
        recipeIngredientList.add(recipeIngredient_2);
        recipe.addRecipeIngredient(recipeIngredient_1);
        recipe.addRecipeIngredient(recipeIngredient_2);
        recipe.setRecipeIngredients(recipeIngredientList);*/

        return recipe;

    }

    @BeforeEach
    public void initTest() {
        recipe = createEntity(em);

    }

    @Test
    public void createRecipe() throws Exception {
        int databaseSizeBeforeCreate = recipeRepository.findAll().size();


        restRecipeMockMvc.perform(post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate + 1);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecipe.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        //assertThat(testRecipe.getRecipeIngredients()).hasSize(2);

    }
    @Test
    public void updateRecipe() throws Exception {
        recipeRepository.saveAndFlush(recipe);
        int databaseSizeBeforeCreate = recipeRepository.findAll().size();

        List<Recipe> recipeList = recipeRepository.findAll();
        Recipe updatedRecipe = recipeList.get(recipeList.size() - 1);

        updatedRecipe.setDescription(UPDATED_DESCRIPTION);

        restRecipeMockMvc.perform(put(ENTITY_API_URL+ "/" + updatedRecipe.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecipe)))
                .andExpect(status().isOk());

        recipeList = recipeRepository.findAll();
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate);

        assertThat(testRecipe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecipe.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

    }

    @Test
    public void getAllRecipes() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);
        //List<Recipe> recipeList = recipeRepository.findAll();

       restRecipeMockMvc
                .perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

/*        restRecipeMockMvc
                .perform(get(ENTITY_API_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(recipe.getId().toString())))
                .andExpect(jsonPath("$.name", is(DEFAULT_NAME)))
                .andExpect(jsonPath("$.description", is(DEFAULT_DESCRIPTION)));*/

    }

    @Test
    public void deleteRecipe() throws Exception {
        recipeRepository.saveAndFlush(recipe);
        int databaseSizeBeforeCreate = recipeRepository.findAll().size();

        List<Recipe> recipeList = recipeRepository.findAll();
        Recipe deletedRecipe = recipeList.get(recipeList.size() - 1);

        restRecipeMockMvc.perform(delete(ENTITY_API_URL + "/" + deletedRecipe.getId().toString()))
                .andExpect(status().isOk());

        recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate - 1);



    }
}