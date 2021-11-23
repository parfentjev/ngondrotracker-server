package com.ngondrotracker.server.user.controller.response;

import com.ngondrotracker.server.user.model.UserTokenDto;

public class SignInResponse extends AuthenticationResponse {
    public SignInResponse(UserTokenDto token, String role) {
        super(token, role);
    }
}
