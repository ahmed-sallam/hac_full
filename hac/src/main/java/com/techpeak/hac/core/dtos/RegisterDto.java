package com.techpeak.hac.core.dtos;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String password;
    // todo add roles
}
