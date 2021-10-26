package com.ngondrotracker.server.service;

import com.ngondrotracker.server.user.model.User;
import com.ngondrotracker.server.user.model.UserRepository;
import com.ngondrotracker.server.user.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends AbstractServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("create user")
    public void createUser() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(new User());
        userService.create("user@host.com", "secretPassword");
        Mockito.verify(userRepository, Mockito.times(1)).save((Mockito.any()));
    }

    @Test
    @DisplayName("user exists - true")
    public void userExists() {
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(new User()));
        Assertions.assertTrue(userService.exists("user@host.com"));
    }

    @Test
    @DisplayName("user exists - false")
    public void userDoesNotExist() {
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertFalse(userService.exists("user@host.com"));
    }
}
