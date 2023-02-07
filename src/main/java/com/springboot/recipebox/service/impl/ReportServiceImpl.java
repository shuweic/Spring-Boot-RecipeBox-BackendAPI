package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.Recipe;
import com.springboot.recipebox.entity.Report;
import com.springboot.recipebox.entity.User;
import com.springboot.recipebox.exception.RecipeAPIException;
import com.springboot.recipebox.exception.ResourceNotFoundException;
import com.springboot.recipebox.payload.RecipeDTO;
import com.springboot.recipebox.payload.ReportDTO;
import com.springboot.recipebox.repository.RecipeRepository;
import com.springboot.recipebox.repository.ReportRepository;
import com.springboot.recipebox.repository.UserRepository;
import com.springboot.recipebox.service.RecipeService;
import com.springboot.recipebox.service.ReportService;
import com.springboot.recipebox.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService
{
    private ReportRepository reportRepository;

    private UserService userService;

    private RecipeService recipeService;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             RecipeService recipeService, UserService userService)
    {
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @Override
    public ReportDTO createReport(String username, ReportDTO reportDTO)
    {
        Report report = mapToEntity(reportDTO);

        User user = userService.getUserbyUsername(username);
        Recipe recipe = recipeService.getRecipeByID(reportDTO.getRecipeID());

        report.setRecipe(recipe);
        report.setUser(user);
        report.setAdminReply("Waiting for administrator response!");

        Report newReport = reportRepository.save(report);

        return mapToDTO(newReport);
    }

    @Override
    public List<ReportDTO> getReportsByRecipeId(int recipeId)
    {
        List<Report> reports = reportRepository.findByRecipeId(recipeId);

        return reports.stream().map(report -> mapToDTO(report)).collect(Collectors.toList());
    }

    @Override
    public ReportDTO getReportByRecipeIdAndReportId(int recipeId, int reportId)
    {
        // retrieve Recipe entity by id
        Recipe recipe = recipeService.getRecipeByID(recipeId);

        // retrieve Report entity by id
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new ResourceNotFoundException("Report", "id", reportId));

        if (!Objects.equals(report.getRecipe().getId(), recipe.getId())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Report does not belong to this Recipe");
        }

        return mapToDTO(report);
    }

    @Override
    public ReportDTO UserUpdateReport(int recipeId, int reportId, ReportDTO reportRequest)
    {
        Recipe recipe = recipeService.getRecipeByID(recipeId);

        // retrieve report by id
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new ResourceNotFoundException("Report", "id", reportId));

        if (!Objects.equals(report.getRecipe().getId(), recipe.getId())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Report does not belong to this Recipe");
        }

        report.setReportTitle(reportRequest.getReportTitle());
        report.setReportDetail(reportRequest.getReportDetail());
        report.setAdminReply("Waiting for administrator response!");

        Report updatedReport = reportRepository.save(report);
        return mapToDTO(updatedReport);
    }

    @Override
    public ReportDTO AdminReplyReport(int recipeId, int reportId, ReportDTO reportRequest)
    {
        Recipe recipe = recipeService.getRecipeByID(recipeId);

        // retrieve report by id
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new ResourceNotFoundException("Report", "id", reportId));

        if (!Objects.equals(report.getRecipe().getId(), recipe.getId())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Report does not belong to this Recipe");
        }

        report.setAdminReply(reportRequest.getAdminReply());

        Report updatedReport = reportRepository.save(report);
        return mapToDTO(updatedReport);
    }



    @Override
    public void deleteReport(int recipeId, int reportId)
    {
        Recipe recipe = recipeService.getRecipeByID(recipeId);

        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new ResourceNotFoundException("Report", "id", reportId));

        if (!Objects.equals(report.getRecipe().getId(), recipe.getId())) {
            throw new RecipeAPIException(HttpStatus.BAD_REQUEST, "Report does not belong to this Recipe");
        }
        reportRepository.delete(report);

    }

    @Override
    public List<RecipeDTO> getAllReportedRecipes()
    {
        List<RecipeDTO> reportedRecipeList = new ArrayList<>();
        List<Integer> reportedRecipesIDList = reportRepository.getReportedRecipes();
        reportedRecipesIDList.forEach(recipeID -> reportedRecipeList.add(recipeService.getRecipeDTOByID(recipeID)));
        return reportedRecipeList;
    }

    @Override
    public List<ReportDTO> getAllReports()
    {
        List<ReportDTO> reportDTOList = new ArrayList<>();
        reportRepository.getAllReports().forEach(report -> reportDTOList.add(mapToDTO(report)));
        return reportDTOList;
    }

    private ReportDTO mapToDTO(Report report)
    {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportID(report.getId());
        reportDTO.setReportTitle(report.getReportTitle());
        reportDTO.setReportDetail(report.getReportDetail());
        reportDTO.setAdminReply(report.getAdminReply());
        reportDTO.setRecipeID(report.getRecipe().getId());

        return reportDTO;
    }

    private Report mapToEntity(ReportDTO reportDTO)
    {
        Report report = new Report();
        report.setId(reportDTO.getReportID());
        report.setReportTitle(reportDTO.getReportTitle());
        report.setReportDetail(reportDTO.getReportDetail());
        report.setAdminReply((reportDTO.getAdminReply()));

        return report;
    }
}
