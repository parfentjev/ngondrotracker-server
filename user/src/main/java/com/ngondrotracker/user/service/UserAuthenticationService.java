package com.ngondrotracker.user.service;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.token.dto.TokenDto;

public interface UserAuthenticationService {
    TokenDto signup(String email, String password) throws ResourceAlreadyExistsException;

    TokenDto signin(String email, String password);
}
