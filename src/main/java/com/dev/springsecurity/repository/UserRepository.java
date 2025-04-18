package com.dev.springsecurity.repository;

import com.dev.springsecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository  extends JpaRepository<User, UUID> {

    Optional<User> findByUsername (String name); //Vai buscar o usuario no BD, pelo "username"
}
