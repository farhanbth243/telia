package com.telia;

import com.telia.entity.User;
import com.telia.repository.UserRepository;
import com.telia.service.UserService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class TeliaApplicationTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Rule
    ExpectedException thrown = ExpectedException.none();

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateUser_Success() {
        User user = new User();
        user.setPersonalNumber("19900101");
        user.setFullName("John Smith");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setEmailAddress("john.smith@example.com");
        user.setPhoneNumber("m123-456-7890");
        Mockito.when(userRepository.findByPersonalNumber(user.getPersonalNumber())).thenReturn(null);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        User createdUser = userService.createUser(user);
        assertEquals(user, createdUser);
    }

    @Test()
    public void testCreateUser_Failure_UserAlreadyExists() {
        User user = new User();
        user.setPersonalNumber("19900101");
        user.setFullName("John Smith");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setEmailAddress("john.smith@example.com");
        user.setPhoneNumber("m123-456-7890");
        Mockito.when(userRepository.findByPersonalNumber(user.getPersonalNumber())).thenReturn(user);
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage(HttpStatus.ALREADY_REPORTED + "User with personal number " + user.getPersonalNumber() + " already exists");
        userService.createUser(user);
    }

    @Test()
    public void testCreateUser_Failure_InvalidEmail() {
        User user = new User();
        user.setPersonalNumber("19900101");
        user.setFullName("John Smith");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setEmailAddress("invalidEmail");
        user.setPhoneNumber("m123-456-7890");
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("email address is invalid");
        userService.createUser(user);
    }
}
