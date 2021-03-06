package com.ngondrotracker.user.service;

import com.ngondrotracker.user.dto.UserDto;

public interface UserService {
    UserDto findUserByEmail(String username);

    UserDto create(String username, String password);
}
