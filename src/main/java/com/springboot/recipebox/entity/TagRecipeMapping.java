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
        name = "tagRecipeMapping"
)
public class TagRecipeMapping implements Serializable
{
    @EmbeddedId
    private TagRecipeMappingKey tagRecipeMappingKey;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class TagRecipeMappingKey implements Serializable
    {
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "recipe_id", nullable = false)
        private Recipe recipe;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumns({
                @JoinColumn(name = "tag_name", referencedColumnName = "tag_name", nullable = false),
                @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
        })
        private Tags tags;
    }
}
