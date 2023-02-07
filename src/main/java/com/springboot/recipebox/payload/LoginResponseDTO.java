package com.springboot.recipebox.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO
{
    String status;
    int userType;
}
