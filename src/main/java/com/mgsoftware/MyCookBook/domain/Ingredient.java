package com.mgsoftware.MyCookBook.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table
public class Ingredient {

    @Id
    @GeneratedValue
    private UUID id;
    @Column
    private String name;

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
}
