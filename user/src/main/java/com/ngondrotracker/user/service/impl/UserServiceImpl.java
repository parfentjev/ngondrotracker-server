package com.ngondrotracker.user.service.impl;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.user.enums.UserRole;
import com.ngondrotracker.user.entity.User;
import com.ngondrotracker.user.repository.UserRepository;
import com.ngondrotracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public User create(String email, String password) throws ResourceAlreadyExistsException {
        if (findUserByEmail(email).isPresent())
            throw new ResourceAlreadyExistsException("User");

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(UserRole.NOT_VERIFIED);

        repository.save(user);

        return user;
    }
}
