package com.ngondrotracker.user.util.mapper;

import com.ngondrotracker.user.dto.UserDto;
import com.ngondrotracker.user.entity.User;

public class UserMapper {
    public UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());

        return userDto;
    }

    public User dtoToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());

        return user;
    }
}
