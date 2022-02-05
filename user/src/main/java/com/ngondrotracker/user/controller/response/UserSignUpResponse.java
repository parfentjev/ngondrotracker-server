package com.ngondrotracker.user.controller.response;

import com.ngondrotracker.token.dto.TokenDto;

public class UserSignUpResponse extends UserAuthenticationResponse {
    public UserSignUpResponse(TokenDto token, String role) {
        super(token, role);
    }
}
