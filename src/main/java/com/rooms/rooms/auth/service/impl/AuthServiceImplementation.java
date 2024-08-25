package com.rooms.rooms.auth.service.impl;

import com.rooms.rooms.auth.dto.LoginResponseDto;
import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.auth.entity.UserAuth;
import com.rooms.rooms.auth.service.AuthService;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Log
@Service
public class AuthServiceImplementation implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthServiceImplementation(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public LoginResponseDto usernameAndPasswordLogin(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserAuth userDetails = (UserAuth) authentication.getPrincipal();
        log.info("Token requested for user :" + userDetails.getUsername() + " with roles: " + userDetails.getAuthorities().toArray()[0]);

        Instant now= Instant.now();
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        log.info("Authorities: " + authorities);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", authorities)
                .claim("username", userDetails.getUsername())
                .build();
        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(jwt);
        loginResponseDto.setRole(RoleName.valueOf(authorities));
        return loginResponseDto;
    }
}
