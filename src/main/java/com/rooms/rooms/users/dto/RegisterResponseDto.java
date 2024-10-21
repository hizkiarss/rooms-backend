package com.rooms.rooms.users.dto;

import com.rooms.rooms.users.entity.Users;
import lombok.Data;

@Data
public class RegisterResponseDto {
     private Long id;
     private String email;
     private String username;
     private String role;
     private String mobileNumber;
}





