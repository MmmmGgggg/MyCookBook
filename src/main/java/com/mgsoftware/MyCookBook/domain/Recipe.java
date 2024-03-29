package com.mgsoftware.MyCookBook.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
public class Recipe {
    @Id
    @GeneratedValue
    @SequenceGenerator(name = "sequenceGenerator")
    @Column
    private UUID id;
    @Column
    private String name;
    @Column
    private String description;
   @OneToMany(mappedBy = "recipe", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
   @JsonIgnore
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    @Column(name = "processed")
    private boolean processed;
    public Recipe() {
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }
    public void setRecipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public Recipe addRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.add(recipeIngredient);
        recipeIngredient.setRecipe(this);
        return this;
    }

    public Recipe removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        //this.recipeIngredients = null;
        this.recipeIngredients.remove(recipeIngredient);
        return this;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
