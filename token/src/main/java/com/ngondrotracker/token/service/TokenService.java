package com.ngondrotracker.token.service;

import com.ngondrotracker.token.ds.TokenDto;

public interface TokenService {
    TokenDto generateToken(String username);

    boolean isValid(String token);

    String getUsernameFromToken(String token);

    TokenDto refreshToken(String currentToken);
}
