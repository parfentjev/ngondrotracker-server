package com.ngondrotracker.server.user.controller.response;

import com.ngondrotracker.server.user.model.UserTokenDto;

public class SignUpResponse extends AuthenticationResponse {
    public SignUpResponse(UserTokenDto token, String role) {
        super(token, role);
    }
}
