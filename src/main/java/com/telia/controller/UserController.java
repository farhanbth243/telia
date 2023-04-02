package com.telia.controller;

import com.telia.entity.User;
import com.telia.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {

        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{personalNumber}")
    public User getUser(@PathVariable String personalNumber) throws ResponseStatusException {
        return userService.getUserByPersonalNumber(personalNumber);
    }

    @PutMapping("/{personalNumber}")
    public ResponseEntity<User> updateUser(@PathVariable String personalNumber, @RequestBody User user) {
        User updatedUser = userService.updateUser(personalNumber, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{personalNumber}")
    public ResponseEntity<Void> deleteUser(@PathVariable String personalNumber) throws ResponseStatusException {
        userService.deleteUser(personalNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sort-by-name")
    public List<User> getAllUsersSortedByName(@RequestParam(defaultValue = "asc") String sort) {
        return userService.getAllUsersSortedByName(sort);
    }

    @GetMapping("/sort-by-personal-number")
    public List<User> getAllUsersSortedByPersonalNumber(@RequestParam(defaultValue = "asc") String sort) {
        return userService.getAllUsersSortedByPersonalNumber(sort);
    }
}
