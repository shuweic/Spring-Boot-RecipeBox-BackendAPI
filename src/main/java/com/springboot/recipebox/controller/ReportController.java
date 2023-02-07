package com.springboot.recipebox.controller;

import com.springboot.recipebox.payload.*;
import com.springboot.recipebox.service.ReportService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "Recipe's report Controller", description = "Rest APIs related to Report Entity for both admin and normal user")

@RestController
@RequestMapping("/api/")
public class ReportController {
    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(path = "/admin/reportedRecipeNamesAndIDs/")
    ResponseEntity<List<RecipeNameDTO>> getAllReportedRecipeNamesAndIDs()
    {
        List<RecipeNameDTO> recipeNameDTOList = new ArrayList<>();

        try
        {
            reportService.getAllReportedRecipes().forEach(
                    recipeDTO -> recipeNameDTOList.add(new RecipeNameDTO(recipeDTO.getId(), recipeDTO.getTitle())));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>((recipeNameDTOList), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/recipeReports/")
    ResponseEntity<List<ReportDTO>> getAllReports()
    {
        List<ReportDTO> reportDTOList;

        try
        {
            reportDTOList = reportService.getAllReports();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>((reportDTOList), HttpStatus.OK);
    }

    @ApiOperation(value = "Post new Report in the System by give a Recipe_id in URL ", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Report Success created|OK"),
            @ApiResponse(code = 401, message = "Report creation not authorized!"),
            @ApiResponse(code = 403, message = "Report creation forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping("{username}/recipe/report")
    public ResponseEntity<ResponseMessageDTO> createReport(@PathVariable(value = "username")
                                                      @ApiParam(name = "username", value = "the username of the user who created the report", example = "user123")
                                                      String username,
                                                           @RequestBody FlagDTO flagDTO)
    {
        ReportDTO reportDTO = new ReportDTO();

        try
        {
            reportDTO.setReportTitle(flagDTO.getTitle());
            reportDTO.setReportDetail(flagDTO.getDetail());
            reportDTO.setRecipeID(flagDTO.getRecipeID());
            reportService.createReport(username, reportDTO);
        }
       catch (Exception e)
       {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }

        return new ResponseEntity<>(new ResponseMessageDTO("success"), HttpStatus.CREATED);
    }

    @GetMapping("/recipes/{recipeId}/reports")
    public List<ReportDTO> getReportsByRecipeId(@PathVariable(value = "recipeId") int recipeId) {
        return reportService.getReportsByRecipeId(recipeId);
    }

    @GetMapping("/recipes/{recipeId}/reports/{reportId}")
    public ResponseEntity<ReportDTO> getCertainReportByRecipeIdAndReportId(@PathVariable(value = "recipeId")
                                                                               @ApiParam(name = "recipeId", value = "the id of recipe that report belong to", example = "2")
                                                                               int recipeId,
                                                           @PathVariable(value = "reportId")
                                                                @ApiParam(name = "reportId", value = "the id of report for getting the certain data", example = "1")
                                                                int reportId) {
        ReportDTO reportDTO = reportService.getReportByRecipeIdAndReportId(recipeId, reportId);
        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }

    @PutMapping("/recipes/{recipeId}/reports/{reportId}/user")
    public ResponseEntity<ReportDTO> userUpdateReport(@PathVariable(value = "recipeId")
                                                          @ApiParam(name = "recipeId", value = "the id of recipe that report belong to", example = "2")
                                                          int recipeId,
                                                    @PathVariable(value = "reportId")
                                                        @ApiParam(name = "reportId", value = "the id of report for getting the certain data", example = "1")
                                                        int reportId,
                                                    @RequestBody ReportDTO reportDTO) {
        ReportDTO updatedReport = reportService.UserUpdateReport(recipeId, reportId, reportDTO);
        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    @PutMapping("/recipes/{recipeId}/reports/{reportId}/admin")
    public ResponseEntity<ReportDTO> adminUpdateReply(@PathVariable(value = "recipeId")
                                                          @ApiParam(name = "recipeId", value = "the id of recipe that report belong to", example = "2")
                                                          int recipeId,
                                                      @PathVariable(value = "reportId")
                                                          @ApiParam(name = "reportId", value = "the id of report for getting the certain data", example = "1")
                                                          int reportId,
                                                      @RequestBody ReportDTO reportDTO) {
        ReportDTO updatedReport = reportService.AdminReplyReport(recipeId, reportId, reportDTO);
        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{recipeId}/reports/{reportId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "recipeId")
                                                    @ApiParam(name = "recipeId", value = "the id of recipe that report belong to", example = "2")
                                                    int recipeId,
                                                @PathVariable(value = "reportId")
                                                    @ApiParam(name = "reportId", value = "the id of report for getting the certain data", example = "1")
                                                    int reportId) {
        reportService.deleteReport(recipeId, reportId);
        return new ResponseEntity<>("report deleted successfully", HttpStatus.OK);
    }


}
