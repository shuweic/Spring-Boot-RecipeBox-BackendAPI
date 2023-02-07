package com.springboot.recipebox.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "rating")
public class Rating
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // rating score of the recipe
    private int score;

    // user that made the recipe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // recipe that this rating is mapped to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
