package com.springboot.recipebox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String body;

    // FetchType.Lazy tell to hibernate to only fetch the related entities from db when we use the relationship
    @ManyToOne(fetch = FetchType.LAZY)
    // add foreign key
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

}
