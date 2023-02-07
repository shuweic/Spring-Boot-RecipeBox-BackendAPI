package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommentDTO {
    @ApiModelProperty(notes = "auto-gen id of a comment", name = "comment_id", required = false)
    private int id;
    @ApiModelProperty(notes = "the name that user wanna leave for their comment", name = "comment name", required = true)
    private String name;
    @ApiModelProperty(notes = "the content that user wanna leave for their comment", name = "comment body", required = true)
    private String body;
}
