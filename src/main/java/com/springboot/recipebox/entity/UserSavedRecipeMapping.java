package com.springboot.recipebox.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "userSavedRecipeMapping"
)
public class UserSavedRecipeMapping implements Serializable {

    @EmbeddedId
    private UserSavedRecipeMappingKey userSavedRecipeMappingKey;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class UserSavedRecipeMappingKey implements Serializable
    {
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne
        @JoinColumn(name = "saved_recipe_id", nullable = false)
        private Recipe recipe;
    }
}
