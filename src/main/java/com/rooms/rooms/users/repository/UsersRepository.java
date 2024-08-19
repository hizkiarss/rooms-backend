package com.rooms.rooms.users.repository;

import com.rooms.rooms.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

}
