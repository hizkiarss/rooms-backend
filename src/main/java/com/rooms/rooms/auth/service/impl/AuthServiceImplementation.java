package com.rooms.rooms.auth.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.rooms.rooms.auth.dto.LoginResponseDto;
import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.auth.entity.UserAuth;
import com.rooms.rooms.auth.service.AuthService;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.exceptions.LoginFailedException;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.stream.Collectors;

import com.google.api.client.json.gson.GsonFactory;


@Log
@Service
public class AuthServiceImplementation implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final UsersService usersService;

    @Value("${SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID}")
    private String CLIENT_ID;


    public AuthServiceImplementation(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, UsersService usersService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.usersService = usersService;
    }

    @Override
    public LoginResponseDto usernameAndPasswordLogin(String username, String password) {
        try {
            if (usersService.findByEmail(username) == null) {
                throw new DataNotFoundException("USER_NOT_FOUND");
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserAuth userDetails = (UserAuth) authentication.getPrincipal();
            log.info("Token requested for user :" + userDetails.getUsername() + " with roles: " + userDetails.getAuthorities().toArray()[0]);

            Instant now = Instant.now();
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
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS");
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException("USER_NOT_FOUND");
        } catch (RuntimeException e) {
            throw new LoginFailedException("Login Failed");
        }
    }


    @Override
    public LoginResponseDto googleLogin(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");

                Users user = usersService.findByEmailGoogleAuth(email);
                if (user == null) {
                    user = new Users();
                    user.setEmail(email);
                    user.setUsername(name);
                    user.setRole(RoleName.USER);
                    user.setIsVerified(true);
                    usersService.save(user);
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(new SimpleGrantedAuthority("USER")));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generate JWT token
                Instant now = Instant.now();
                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plus(10, ChronoUnit.HOURS))
                        .subject(email)
                        .claim("scope", "ROLE_USER")
                        .claim("username", email)
                        .build();
                String jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

                LoginResponseDto loginResponseDto = new LoginResponseDto();
                loginResponseDto.setToken(jwt);
                loginResponseDto.setRole(RoleName.USER);
                return loginResponseDto;
            } else {
                throw new RuntimeException("Invalid ID token.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during Google authentication: " + e.getMessage());
        }
    }
}

