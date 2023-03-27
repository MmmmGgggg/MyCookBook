package com.mgsoftware.MyCookBook.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
public class Recipe {

    private UUID id;
    private String name;
    private String description;

    public Recipe() {
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


}
