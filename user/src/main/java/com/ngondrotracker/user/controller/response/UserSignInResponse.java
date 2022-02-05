package com.ngondrotracker.user.controller.response;

import com.ngondrotracker.token.dto.TokenDto;

public class UserSignInResponse extends UserAuthenticationResponse {
    public UserSignInResponse(TokenDto token, String role) {
        super(token, role);
    }
}
