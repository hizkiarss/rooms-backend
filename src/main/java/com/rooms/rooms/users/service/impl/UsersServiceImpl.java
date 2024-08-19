package com.rooms.rooms.users.service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.repository.UsersRepository;
import com.rooms.rooms.users.service.UsersService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
     private UsersRepository usersRepository;
     public UsersServiceImpl(UsersRepository usersRepository){
          this.usersRepository = usersRepository;
     }

     @Override
     @Cacheable(value = "getUsersById", key = "#id")
     public Users getUsersById(Long id){
          Optional<Users> users = usersRepository.findById(id);
          if(users.isEmpty()){
               throw new DataNotFoundException("User with id " + id +" not found");
          }
          return users.orElse(null);
     }
}
