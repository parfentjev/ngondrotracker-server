package com.ngondrotracker.server.user.controller.response;

import com.ngondrotracker.server.user.model.UserTokenDto;

public class UserSignUpResponse extends UserAuthenticationResponse {
    public UserSignUpResponse(UserTokenDto token, String role) {
        super(token, role);
    }
}
