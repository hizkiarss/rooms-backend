package com.rooms.rooms.users.dto;

import com.rooms.rooms.users.entity.Users;

public class Mapper {

     public static RegisterResponseDto entityToDto(Users user) {
          if (user == null) {
               return null;
          }

          RegisterResponseDto dto = new RegisterResponseDto();
          dto.setId(user.getId());
          dto.setEmail(user.getEmail());
          dto.setUsername(user.getUsername());
          dto.setRole(user.getRole() != null ? user.getRole().name() : null);
          dto.setMobileNumber(user.getMobileNumber());

          return dto;
     }
}
