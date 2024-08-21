package com.rooms.rooms.role.repository;

import com.rooms.rooms.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
