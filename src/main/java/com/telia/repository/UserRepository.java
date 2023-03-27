package com.telia.repository;

import com.telia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByPersonalNumber(Long personalNumber);
}
