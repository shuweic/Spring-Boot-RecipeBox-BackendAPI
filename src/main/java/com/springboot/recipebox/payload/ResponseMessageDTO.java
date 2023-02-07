package com.springboot.recipebox.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessageDTO
{
    @ApiModelProperty(notes = "response string message", name = "message", required = true)
    String message;
}
