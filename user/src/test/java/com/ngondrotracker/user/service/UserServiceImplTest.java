package com.ngondrotracker.user.service;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.user.dto.UserDto;
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
    public void createUser() throws ResourceAlreadyExistsException {
        final String email = "user@host.com";
        final String password = "userPassword";

        UserDto newUser = userService.create(email, password);
        verify(userRepository, times(1)).save(any());
        assertEquals(email, newUser.getEmail());
    }

    @Test
    public void createUserThatAlreadyExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.create(null, null));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void userExists() {
        when(userRepository.findByEmail("user@host.com")).thenReturn(Optional.of(new User()));
        UserDto userDto = userService.findUserByEmail("user@host.com");
        assertEquals("user@host.com", userDto.getEmail());
    }

    @Test
    public void userDoesNotExist() {
        when(userRepository.findByEmail("user@host.com")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findUserByEmail("user@host.com"));
    }
}
