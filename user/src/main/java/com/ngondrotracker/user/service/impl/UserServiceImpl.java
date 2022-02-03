package com.ngondrotracker.user.service.impl;

import com.ngondrotracker.user.enums.UserRole;
import com.ngondrotracker.user.ds.User;
import com.ngondrotracker.user.repository.UserRepository;
import com.ngondrotracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
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
