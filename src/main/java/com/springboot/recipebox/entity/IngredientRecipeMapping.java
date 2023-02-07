package com.springboot.recipebox.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "ingredientRecipeMapping" //, uniqueConstraints = {@UniqueConstraint(columnNames = {"ingredient_name"})}
)
public class IngredientRecipeMapping implements Serializable {

    @EmbeddedId
    private IngredientRecipeMappingKey ingredientRecipeMappingKey;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class IngredientRecipeMappingKey implements Serializable
    {
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "recipe_id", nullable = false)
        private Recipe recipe;

        @Column(name = "ingredient_name")
        private String ingredientName;
    }

    @Column(name = "amount")
    private int amount;

    @Column(name = "unit")
    private String unit;
}
