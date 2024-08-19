package com.rooms.rooms.status.repository;

import com.rooms.rooms.status.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
