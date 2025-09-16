package com.example.secure_api_lab1.repository;

import com.example.secure_api_lab1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);
}
