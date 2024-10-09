package com.rooms.rooms.auth.controller;

import com.rooms.rooms.auth.dto.LoginResponseDto;
import com.rooms.rooms.auth.service.AuthService;
import jakarta.annotation.security.PermitAll;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class AuthResolver {
    private final AuthService authService;

    public AuthResolver(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping (value ="login")
    public LoginResponseDto create(@Argument("email") String username, @Argument ("password") String password) {
        return authService.usernameAndPasswordLogin(username, password);
}

    @MutationMapping (value ="googleLogin")
    public LoginResponseDto googleLogin(@Argument("idToken") String idToken) {
        return authService.googleLogin(idToken);
    }

}
