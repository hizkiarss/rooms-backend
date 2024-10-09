package com.rooms.rooms.role.service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.role.entity.Role;
import com.rooms.rooms.role.repository.RoleRepository;
import com.rooms.rooms.role.service.RoleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
     private RoleRepository roleRepository;
     public RoleServiceImpl(RoleRepository roleRepository){
          this.roleRepository = roleRepository;
     }

     @Override
     @Cacheable(value = "getRoleById", key = "#id")
     public Role getRoleById(Long id){
          Optional<Role> role = roleRepository.findById(id);
          if(role.isEmpty()){
               throw new DataNotFoundException("role with id " + id +" not found");
          }
          return role.orElse(null);
     }



}
