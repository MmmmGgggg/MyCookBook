package com.mgsoftware.MyCookBook.service.dto;

import com.mgsoftware.MyCookBook.domain.Ingredient;
import com.mgsoftware.MyCookBook.domain.RecipeIngredient;
import com.mgsoftware.MyCookBook.domain.Unit;

public class RecipeIngredientDTO {

    final private String ingredient;
    String unit;
    Double quantity;

    public RecipeIngredientDTO(String ingredient, Double quantity, String unit) {
        this.ingredient = ingredient;
        this.unit = unit;
        this.quantity = quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getUnit() {
        return unit;
    }

    public Double getQuantity() {
        return quantity;
    }

}
