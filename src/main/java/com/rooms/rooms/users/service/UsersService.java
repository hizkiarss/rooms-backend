package com.rooms.rooms.users.service;

import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.users.dto.RegisterRequestDto;
import com.rooms.rooms.users.dto.RegisterResponseDto;
import com.rooms.rooms.users.entity.Users;

public interface UsersService {
     Users getUsersById(Long id);
     RegisterResponseDto register(RegisterRequestDto dto, RoleName role);
     ;
     Users findByEmail(String email);
     void deleteUserByEmail(String email, String password);
}
