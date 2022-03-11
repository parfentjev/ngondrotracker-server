package com.ngondrotracker.user.service;

import com.ngondrotracker.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmail(String username);

    User create(String username, String password) throws ItemAlreadyExistsException;
}
