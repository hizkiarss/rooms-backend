package com.rooms.rooms.users.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
     private String oldPassword;
     private String newPassword;
}
