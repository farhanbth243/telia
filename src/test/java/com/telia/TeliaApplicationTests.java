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

    @Test()
    public void testCreateUser_Failure_UserAlreadyExists() {
        User user = new User("19900101", "John Smith", LocalDate.of(1990, 1, 1),
                "john.smith@example.com", "123-456-7890");
        Mockito.when(userRepository.findByPersonalNumber(user.getPersonalNumber())).thenReturn(Optional.of(user));
        userService.createUser(user);
    }

    @Test()
    public void testCreateUser_Failure_InvalidEmail() {
        User user = new User("19900101", "John Smith", LocalDate.of(1990, 1, 1),
                "john.smith@example.com", "123-456-7890");
        userService.createUser(user);
    }

    @Test
    public void testGetAllUsersSortedByName() {
        User user1 = new User("19900101", "John Smith", LocalDate.of(1990, 1, 1),
                "john.smith@example.com", "123-456-7890");
        User user2 = new User("19950505", "Alice Cooper", LocalDate.of(1995, 5, 5),
                "alice.cooper@example.com", "987-654-3210");
        User user3 = new User("19921231", "Bob Johnson", LocalDate.of(1992, 12, 31),
                "bob.johnson@example.com", "555-555-5555");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> users = userService.getAllUsersSortedByName("asc");

        assertFalse(users.isEmpty());

        assertEquals(3, users.size());

        assertEquals("Alice Cooper", users.get(0).getFullName());
        assertEquals("Bob Johnson", users.get(1).getFullName());
        assertEquals("John Smith", users.get(2).getFullName());
    }
}
