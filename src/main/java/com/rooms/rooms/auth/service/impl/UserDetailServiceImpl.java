package com.rooms.rooms.auth.service.impl;

import com.rooms.rooms.auth.entity.UserAuth;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.repository.UsersRepository;
import com.rooms.rooms.users.service.UsersService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;
    public UserDetailServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users userData = usersRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + email));
        return new UserAuth(userData);
    }
}
