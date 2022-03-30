package com.ngondrotracker.user.util;

import com.ngondrotracker.user.util.mapper.UserMapper;

public class UserUtils {
    public static UserMapper userMapper() {
        return new UserMapper();
    }
}
