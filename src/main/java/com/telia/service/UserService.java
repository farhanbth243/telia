package com.telia.service;

import com.telia.entity.User;
import com.telia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws ResponseStatusException {
        User existingUser = userRepository.findByPersonalNumber(user.getPersonalNumber());
        if (existingUser!=null) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "User with personal number " + user.getPersonalNumber() + " already exists");
        }

        // Save new user
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setPhoneNumber(null);
            user.setEmailAddress(null);
        }
        return users;
    }

    public void deleteUser(String personalNumber) throws ResponseStatusException {
        User existingUser = userRepository.findByPersonalNumber(personalNumber);
        if (existingUser==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with personal number " + personalNumber + " not found");
        }

        User user = userRepository.findByPersonalNumber(personalNumber);
        userRepository.deleteById(user.getId());
    }

    public User updateUser(String personalNumber, User user) {
        User existingUser = userRepository.findByPersonalNumber(personalNumber);
        if (existingUser==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with personal number " + personalNumber + " not found");
        }

        existingUser.setPersonalNumber(user.getPersonalNumber());
        existingUser.setFullName(user.getFullName());
        existingUser.setBirthDate(user.getBirthDate());
        existingUser.setEmailAddress(user.getEmailAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    public List<User> getAllUsersSortedByName(String direction) {
        List<User> users = direction.equals("asc")
                ? userRepository.findAll(Sort.by("fullName").ascending())
                : userRepository.findAll(Sort.by("fullName").descending());
        for (User user : users) {
            user.setPhoneNumber(null);
            user.setEmailAddress(null);
        }
        return users;
    }

    public List<User> getAllUsersSortedByPersonalNumber(String direction) {
        List<User> users = direction.equals("asc")
                ? userRepository.findAll(Sort.by("personalNumber").ascending())
                : userRepository.findAll(Sort.by("personalNumber").descending());
        for (User user : users) {
            user.setPhoneNumber(null);
            user.setEmailAddress(null);
        }
        return users;
    }
}