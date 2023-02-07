package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Pantry;
import com.springboot.recipebox.entity.Pantry.PantryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PantryRepository extends JpaRepository<Pantry, PantryKey> {
//    List<Ingredient> findAllByPantryKeyUserUsername(String username);
    List<Pantry> findAllByPantryKeyUserUsername(String username);

    Pantry findByPantryKeyUserUsernameAndPantryKeyIngredientName(String username, String ingredientName);
}
