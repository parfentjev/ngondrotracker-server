package com.ngondrotracker.user.service;

import com.ngondrotracker.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.token.dto.TokenDto;

public interface UserAuthenticationService {
    TokenDto signup(String email, String password) throws ItemAlreadyExistsException;

    TokenDto signin(String email, String password);
}
