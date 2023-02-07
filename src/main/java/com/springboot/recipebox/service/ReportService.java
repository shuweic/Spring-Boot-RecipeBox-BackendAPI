package com.springboot.recipebox.service;

import com.springboot.recipebox.payload.RecipeDTO;
import com.springboot.recipebox.payload.ReportDTO;

import java.util.List;

public interface ReportService {
    ReportDTO createReport(String username, ReportDTO reportDTO);

    List<ReportDTO> getReportsByRecipeId(int recipeId);

    ReportDTO getReportByRecipeIdAndReportId(int recipeId, int reportId);

    ReportDTO UserUpdateReport(int recipeId, int reportId, ReportDTO reportRequest);

    ReportDTO AdminReplyReport(int recipeId, int reportId, ReportDTO reportRequest);

    void deleteReport(int recipeId, int reportId);

    List<RecipeDTO> getAllReportedRecipes();

    List<ReportDTO> getAllReports();
}
