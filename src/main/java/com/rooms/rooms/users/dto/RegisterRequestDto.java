package com.rooms.rooms.users.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;
    private String username;

}

