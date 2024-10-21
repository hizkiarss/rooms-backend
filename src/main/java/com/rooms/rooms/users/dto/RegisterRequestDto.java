package com.rooms.rooms.users.dto;

import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.users.entity.Users;
import lombok.Data;

@Data
public class RegisterRequestDto {
     private String email;
     private String username;
     private String password;
     private String mobileNumber;
     private String role;

     public Users toEntity() {
          Users user = new Users();
          user.setEmail(this.email);
          user.setUsername(this.username);
          user.setPassword(this.password);
          user.setMobileNumber(this.mobileNumber);
          user.setRole(RoleName.valueOf(this.role));
          return user;
     }
}

