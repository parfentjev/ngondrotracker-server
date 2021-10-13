package com.ngondrotracker.server.user.service;

import com.ngondrotracker.server.user.enums.UserRole;
import com.ngondrotracker.server.user.model.User;
import com.ngondrotracker.server.user.model.UserRepository;
import com.ngondrotracker.server.user.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    public boolean exists(String email) {
        return repository.findByEmail(email).isPresent();
    }

    @Override
    public void create(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(UserRole.NOT_VERIFIED);

        repository.save(user);
    }
}
