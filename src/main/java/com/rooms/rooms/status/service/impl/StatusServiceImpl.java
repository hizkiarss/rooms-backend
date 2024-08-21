package com.rooms.rooms.status.service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.status.entity.Status;
import com.rooms.rooms.status.repository.StatusRepository;
import com.rooms.rooms.status.service.StatusService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {
     private StatusRepository statusRepository;
     public StatusServiceImpl(StatusRepository statusRepository){
          this.statusRepository = statusRepository;
     }

     @Override
     @Cacheable(value = "getStatusById", key = "#id")
     public Status getStatusById(Long id){
          Optional<Status> status = statusRepository.findById(id);
          if(status.isEmpty()){
               throw new DataNotFoundException("Status with id " + id + " not found");
          }
          return status.orElse(null);
     }
}
