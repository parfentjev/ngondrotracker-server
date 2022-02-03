package com.ngondrotracker.user.controller.response;

import com.ngondrotracker.token.ds.TokenDto;

public class UserSignUpResponse extends UserAuthenticationResponse {
    public UserSignUpResponse(TokenDto token, String role) {
        super(token, role);
    }
}
