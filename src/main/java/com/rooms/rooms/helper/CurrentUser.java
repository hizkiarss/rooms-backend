package com.rooms.rooms.helper;

import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {
     private final UsersService usersService;
     public CurrentUser(UsersService usersService) {
          this.usersService = usersService;
     }
     public Long getCurrentUserId() {
          SecurityContext securityContext = SecurityContextHolder.getContext();
          Authentication authentication = securityContext.getAuthentication();
          String requesterEmail = authentication.getName();
          Users users = usersService.findByEmail(requesterEmail);
          return users.getId();
     }
}
