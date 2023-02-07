package com.springboot.recipebox.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "instructions" //, uniqueConstraints = {@UniqueConstraint(columnNames = {"ingredient_name"})}
)
public class Instructions {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "instructions_id", nullable = false)
    private int id;

    // many rows of instructions are associated to one recipe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "instructions_order", nullable = false)
    private int order;
}