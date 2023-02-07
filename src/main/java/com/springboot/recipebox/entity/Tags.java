package com.springboot.recipebox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "tags"
)
public class Tags
{
    @EmbeddedId
    private TagKey tagKey;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class TagKey implements Serializable
    {

        @Column(name = "tag_name", nullable = false)
        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "tagRecipeMappingKey.tags", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TagRecipeMapping> tagRecipeMappings = new java.util.LinkedHashSet<>();
}
