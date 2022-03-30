package com.ngondrotracker.user.service.impl;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.user.dto.UserDto;
import com.ngondrotracker.user.entity.User;
import com.ngondrotracker.user.enums.UserRole;
import com.ngondrotracker.user.repository.UserRepository;
import com.ngondrotracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ngondrotracker.user.util.UserUtils.userMapper;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDto findUserByEmail(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return userMapper().entityToDto(user);
    }

    @Override
    public UserDto create(String email, String password) throws ResourceAlreadyExistsException {
        if (repository.findByEmail(email).isPresent())
            throw new ResourceAlreadyExistsException("User");

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(UserRole.NOT_VERIFIED);

        repository.save(user);

        return userMapper().entityToDto(user);
    }
}
