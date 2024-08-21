package com.rooms.rooms.users.service.impl;

import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.exceptions.InvalidPasswordException;
import com.rooms.rooms.users.dto.Mapper;
import com.rooms.rooms.users.dto.RegisterRequestDto;
import com.rooms.rooms.users.dto.RegisterResponseDto;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.repository.UsersRepository;
import com.rooms.rooms.users.service.UsersService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users getUsersById(Long id)  {
        Optional<Users> users = usersRepository.findById(id);
        return users.orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    @Override
    public RegisterResponseDto register(RegisterRequestDto dto, RoleName role) {
       if (usersRepository.findByEmail(dto.getEmail()).isPresent()) {
           throw new IllegalArgumentException("Email is already in use");
       }
        Users newUser = new Users();
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setUsername(dto.getUsername());
        newUser.setRole(role);
        Users savedusers = usersRepository.save(newUser);

        RegisterResponseDto response = Mapper.entityToDto(savedusers);
        return response;
    }

    @Override
    public Users findByEmail(String email) {
        Optional<Users> users = usersRepository.findByEmail(email);
        return users.orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    @Override
    public void deleteUserByEmail(String email , String password) {
        Users currentUser = usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, currentUser.getPassword())) {
            throw new InvalidPasswordException("Invalid password provided");
        }
        currentUser.setDeletedAt(Instant.now());
        usersRepository.save(currentUser);
    }





}
