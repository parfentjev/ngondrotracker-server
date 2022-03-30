package com.ngondrotracker.user.dto;

import com.ngondrotracker.user.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String email;

    private UserRole role;
}
