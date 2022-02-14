package com.ngondrotracker.user.service;

import com.ngondrotracker.user.entity.User;
import com.ngondrotracker.user.repository.UserRepository;
import com.ngondrotracker.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void create() {
        final String email = "user@host.com";
        final String password = "userPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.save(any())).thenReturn(user);
        User newUser = userService.create(email, password);
        verify(userRepository, times(1)).save((any()));
        assertEquals(email, newUser.getEmail());
        assertEquals(password, newUser.getPassword());
    }

    @Test
    public void userExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        assertTrue(userService.findUserByEmail("user@host.com").isPresent());
    }

    @Test
    public void userDoesNotExist() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertFalse(userService.findUserByEmail("user@host.com").isPresent());
    }
}
