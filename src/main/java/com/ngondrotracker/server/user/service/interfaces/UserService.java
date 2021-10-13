package com.ngondrotracker.server.user.service.interfaces;

public interface UserService {
    boolean exists(String username);

    void create(String username, String password);
}
