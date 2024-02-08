package com.mgsoftware.MyCookBook.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity
@Table
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecipeSearch {

    @Id
    @GeneratedValue
    @SequenceGenerator(name = "sequenceGenerator")
    @Column
    private UUID id;


    @NotNull
    @Column(name = "ingredientsCombination", nullable = false)
    private String ingredientsCombination;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "recipeSearch" }, allowSetters = true)
    private Recipe recipe;

    @Column(name = "nrCombinations")
    private int nrCombinations;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIngredientsCombination() {
        return ingredientsCombination;
    }

    public void setIngredientsCombination(String ingredientsCombination) {
        this.ingredientsCombination = ingredientsCombination;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getNrCombinations() {
        return nrCombinations;
    }

    public void setNrCombinations(int nrCombinations) {
        this.nrCombinations = nrCombinations;
    }
}
