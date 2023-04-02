package com.telia;

import com.telia.entity.User;
import com.telia.repository.UserRepository;
import com.telia.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class TeliaApplicationTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;


    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateUser_Success() {
        User user = new User("19900101", "John Smith", LocalDate.of(1990, 1, 1),
                "john.smith@example.com", "123-456-7890");
        Mockito.when(userRepository.findByPersonalNumber(user.getPersonalNumber())).thenReturn(null);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        User createdUser = userService.createUser(user);
        assertEquals(user, createdUser);
    }

    @Test(expected = ResponseStatusException.class)
    public void testCreateUser_Failure_UserAlreadyExists() {
        User user = new User("19900101", "John Smith", LocalDate.of(1990, 1, 1),
                "john.smith@example.com", "123-456-7890");
        Mockito.when(userRepository.findByPersonalNumber(user.getPersonalNumber())).thenReturn(user);
        userService.createUser(user);
    }

    @Test()
    public void testCreateUser_Failure_InvalidEmail() {
        User user = new User("19900101", "John Smith", LocalDate.of(1990, 1, 1),
                "john.smith@example.com", "123-456-7890");
        userService.createUser(user);
    }
}
