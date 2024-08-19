package com.rooms.rooms.properties.service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.repository.PropertiesRepository;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.users.repository.UsersRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropertiesServiceImpl implements PropertiesService {
     private PropertiesRepository propertiesRepository;

     public PropertiesServiceImpl(PropertiesRepository propertiesRepository){
          this.propertiesRepository = propertiesRepository;
     }

     @Override
     @Cacheable(value = "getPropertiesById", key = "#id")
     public Properties getPropertiesById(Long id){
          Optional<Properties> properties = propertiesRepository.findById(id);
          if(properties.isEmpty()){
               throw new DataNotFoundException("Properties with id " + id + " not found");
          }
          return properties.orElse(null);
     }
}
