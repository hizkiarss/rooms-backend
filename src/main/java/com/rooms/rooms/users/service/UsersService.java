package com.rooms.rooms.users.service;

import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.users.dto.RegisterRequestDto;
import com.rooms.rooms.users.dto.RegisterResponseDto;
import com.rooms.rooms.users.dto.ResetPasswordDto;
import com.rooms.rooms.users.dto.UpdateUserDto;
import com.rooms.rooms.users.entity.Users;

public interface UsersService {
     Users getUsersById(Long id);

     String register(RegisterRequestDto dto, RoleName role);

     Users findByEmail(String email);

     void deleteUserByEmail(String email, String password);

     Users save(Users user);

     Users findByEmailGoogleAuth(String email);

     RegisterResponseDto verifyEmail(String email);

     String sendResetPasswordLink(String email);

     String resetPassword(String email, ResetPasswordDto dto);

     Users uploadAvatar(String email, String imgUrl);

     Users updateUserInformation(UpdateUserDto dto, String email);
}
