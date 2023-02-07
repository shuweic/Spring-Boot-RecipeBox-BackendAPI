package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReportDTO
{
    @ApiModelProperty(notes = "id of report",name="reportId",required=false)
    private int reportID;
    @ApiModelProperty(notes = "title of report",name="reportId",required=true,value = "test title")
    private String reportTitle;
    @ApiModelProperty(notes = "title of report",name="reportTitle",required=true,value = "test reportDetail")
    private String reportDetail;
    @ApiModelProperty(notes = "Detail of report",name="reportDetail",required=false)
    private String adminReply;
    @ApiModelProperty(notes = "recipeID provide in URL",name="adminReply",required=false)
    private int recipeID;
}
