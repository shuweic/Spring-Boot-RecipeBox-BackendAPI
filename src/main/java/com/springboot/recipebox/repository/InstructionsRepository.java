package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Instructions;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface InstructionsRepository extends JpaRepository<Instructions, Integer>
{
    List<Instructions> getAllByRecipeId(int recipeID);
}
