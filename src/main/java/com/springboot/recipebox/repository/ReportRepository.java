package com.springboot.recipebox.repository;

import com.springboot.recipebox.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer>
{
    List<Report> findByRecipeId(int recipeID);

    @Query("select r from Report r")
    List<Report> getAllReports();

    @Query("select DISTINCT r.recipe.id from Report r")
    List<Integer> getReportedRecipes();
}
