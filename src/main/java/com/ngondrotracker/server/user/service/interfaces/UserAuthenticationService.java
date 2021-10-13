package com.ngondrotracker.server.user.service.interfaces;

import com.ngondrotracker.server.user.model.UserTokenDto;

public interface UserAuthenticationService {
    UserTokenDto signup(String email, String password);

    UserTokenDto signin(String email, String password);
}
