package com.telia.repository;

import com.telia.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByPersonalNumber(String personalNumber);

    List<User> findAllByFullNameContainingIgnoreCaseAndPersonalNumber(String name, String personalNumber, Sort sort);

    List<User> findAllByFullNameContainingIgnoreCase(String name, Sort sort);
}
