package com.springboot.recipebox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        name = "pantry" //, uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class Pantry implements Serializable
{
    @EmbeddedId
    private PantryKey pantryKey;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class PantryKey implements Serializable
    {
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(name = "ingredient_name", nullable = false)
        private String ingredientName;
    }
}
