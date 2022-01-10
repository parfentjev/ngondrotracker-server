package com.ngondrotracker.server.service.user;

import com.ngondrotracker.server.service.AbstractServiceTest;
import com.ngondrotracker.server.user.model.User;
import com.ngondrotracker.server.user.model.UserRepository;
import com.ngondrotracker.server.user.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends AbstractServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("create user")
    public void createUser() {
        when(userRepository.save(any())).thenReturn(new User());
        userService.create("user@host.com", "secretPassword");
        verify(userRepository, times(1)).save((any()));
    }

    @Test
    @DisplayName("user exists - true")
    public void userExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        assertTrue(userService.exists("user@host.com"));
    }

    @Test
    @DisplayName("user exists - false")
    public void userDoesNotExist() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertFalse(userService.exists("user@host.com"));
    }
}
