package com.ngondrotracker.server.user.service.interfaces;

import com.ngondrotracker.server.user.model.UserTokenDto;

public interface UserTokenService {
    UserTokenDto generateToken(String username);

    boolean isValid(String token);

    String getUsernameFromToken(String token);

    UserTokenDto refreshToken(String currentToken);
}
