package com.ngondrotracker.user.controller.response;

import com.ngondrotracker.token.dto.TokenDto;

public class UserAuthenticationResponse {
    private final String token;
    private final Long expirationDate;
    private final String roles;

    public UserAuthenticationResponse(TokenDto token, String role) {
        this.token = token.getToken();
        this.expirationDate = token.getExpirationDate();
        this.roles = role;
    }


    public String getToken() {
        return token;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public String getRoles() {
        return roles;
    }
}
