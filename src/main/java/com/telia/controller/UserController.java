package com.telia.controller;

import com.telia.entity.User;
import com.telia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        // Check if user with same personal number already exists
        Optional<User> existingUser = repository.findByPersonalNumber(user.getPersonalNumber());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with personal number " + user.getPersonalNumber() + " already exists");
        }

        // Save new user
        return repository.save(user);
    }

    @PostMapping("/createUsers")
    public List<User> addUsers(@RequestBody List<User> users) {
        return repository.saveAll(users);
    }

    @GetMapping("users")
    public List<User> getUsers() {
        return repository.findAll();
    }

    @GetMapping("users/{personalNumber}")
    public User getUser(@PathVariable Long personalNumber) {
        return repository.findByPersonalNumber(personalNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{personalNumber}")
    public User updateUser(@PathVariable int personalNumber, @RequestBody User user) {
        User existingUser = repository.findById(personalNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existingUser.setPersonalNumber(user.getPersonalNumber());
        existingUser.setFullName(user.getFullName());
        existingUser.setBirthDate(user.getBirthDate());
        existingUser.setEmailAddress(user.getEmailAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        return repository.save(existingUser);
    }

    @DeleteMapping("/delete/{personalNumber}")
    public void deleteUser(@PathVariable int personalNumber) {
        repository.deleteById(personalNumber);
    }
}