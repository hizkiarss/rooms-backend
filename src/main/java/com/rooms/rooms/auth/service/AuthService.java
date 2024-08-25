package com.rooms.rooms.auth.service;

import com.rooms.rooms.auth.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto usernameAndPasswordLogin(String username, String password);
    LoginResponseDto googleLogin(String idToken);
}
