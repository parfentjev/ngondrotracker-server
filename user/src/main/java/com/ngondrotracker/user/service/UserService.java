package com.ngondrotracker.user.service;

public interface UserService {
    boolean exists(String username);

    void create(String username, String password);
}
