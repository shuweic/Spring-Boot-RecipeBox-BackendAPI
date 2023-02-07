package com.springboot.recipebox.payload;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MessageDTO {
    private int message_id;
    private String content;
    private LocalDate date_sent;
}
