package com.ngondrotracker.server.user.controller.response;

import com.ngondrotracker.server.user.model.UserTokenDto;

public class UserSignInResponse extends UserAuthenticationResponse {
    public UserSignInResponse(UserTokenDto token, String role) {
        super(token, role);
    }
}
