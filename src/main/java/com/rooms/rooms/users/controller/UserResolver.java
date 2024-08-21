package com.rooms.rooms.users.controller;

import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.users.dto.RegisterRequestDto;
import com.rooms.rooms.users.dto.RegisterResponseDto;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class UserResolver {
    private final UsersService usersService;

    public UserResolver(UsersService usersService) {
        this.usersService = usersService;
    }

    @PreAuthorize("permitAll()")
    @MutationMapping(value = "userRegister")
    public RegisterResponseDto userRegister(@Argument ("input") RegisterRequestDto registerRequestDto) {
        return usersService.register(registerRequestDto, RoleName.USER);
    }

    @PreAuthorize("permitAll()")
    @MutationMapping(value = "tenantRegister")
    public RegisterResponseDto tenantRegister(@Argument ("input") RegisterRequestDto registerRequestDto) {
        return usersService.register(registerRequestDto,RoleName.TENANT);
    }

    @MutationMapping(value ="delete")
    public void deleteAccount(@Argument String email, @Argument String password) {
        usersService.deleteUserByEmail(email, password);
    }

    @QueryMapping(value = "findUserByEmail")
    public Users findUserByEmail(@Argument String email) {
        return usersService.findByEmail(email);
    }

    @QueryMapping(value = "findUserById")
    public Users findUserById(@Argument Long id) {
        return usersService.getUsersById(id);
    }

}
