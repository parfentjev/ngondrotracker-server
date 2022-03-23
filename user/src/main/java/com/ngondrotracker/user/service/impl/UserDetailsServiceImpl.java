package com.ngondrotracker.user.service.impl;

import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.user.entity.User;
import com.ngondrotracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
