package com.telia.service;

import com.telia.entity.User;
import com.telia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws ResponseStatusException {
        Optional<User> existingUser = userRepository.findByPersonalNumber(user.getPersonalNumber());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with personal number " + user.getPersonalNumber() + " already exists");
        }

        // Save new user
        return userRepository.save(user);
    }

    public User getUserByPersonalNumber(String personalNumber) throws ResponseStatusException {
        User user = userRepository.findByPersonalNumber(personalNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + personalNumber));
        user.setPhoneNumber(null);
        user.setEmailAddress(null);
        return user;
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
        userRepository.findByPersonalNumber(personalNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with personal number " + personalNumber + " not found"));

        User user = getUserByPersonalNumber(personalNumber);

        userRepository.deleteById(user.getId());
    }

    public User updateUser(String personalNumber, User user) {
        User existingUser = userRepository.findByPersonalNumber(personalNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with personal number " + personalNumber + " not found"));

        existingUser.setPersonalNumber(user.getPersonalNumber());
        existingUser.setFullName(user.getFullName());
        existingUser.setBirthDate(user.getBirthDate());
        existingUser.setEmailAddress(user.getEmailAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    public List<User> findAll(String name, String personalNumber, String direction) {
        Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, "fullName");
        if (personalNumber != null) {
            return userRepository.findAllByFullNameContainingIgnoreCaseAndPersonalNumber(name, personalNumber, sort);
        }
        return userRepository.findAllByFullNameContainingIgnoreCase(name, sort);
    }
}
