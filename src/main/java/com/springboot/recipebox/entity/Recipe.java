package com.springboot.recipebox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "recipe", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class Recipe
{
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "recipe_id", nullable = false)
    private int id;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "summary", length = 5000)
    private String summary;

    @Column(name = "cooking_minutes")
    private int cookingMinutes;

    @Column(name = "servings")
    private int servings;

    @Column(name = "extension")
    private String extension;

    // user - for whose post it is; all recipes in the recipe table are created by user
    // many rows of recipe are associated to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // https://www.baeldung.com/jpa-cascade-types When we perform some action on the target entity, the same action will be applied to the associated entity.
    @JsonIgnore
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new java.util.LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "ingredientRecipeMappingKey.recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IngredientRecipeMapping> ingredientRecipeMappings = new java.util.LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tagRecipeMappingKey.recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TagRecipeMapping> tagRecipeMappings = new java.util.LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userSavedRecipeMappingKey.recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSavedRecipeMapping> userSavedRecipeMappings = new java.util.LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Instructions> instructions = new java.util.LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rating> ratings = new java.util.LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Report> reports = new java.util.LinkedHashSet<>();


    // Recipe image
    @JsonIgnore
    @Lob
    private Blob image;


    // Ingredient recipe mapping is in another entity
    // Instructions recipe mapping is defined in another entity

}
