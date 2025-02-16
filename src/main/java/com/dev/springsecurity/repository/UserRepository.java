package com.dev.springsecurity.repository;

import com.dev.springsecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository  extends JpaRepository<User, UUID> {
}
