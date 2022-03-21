package com.ngondrotracker.user.controller.response;

import com.ngondrotracker.token.dto.TokenDto;
import lombok.Data;

@Data
public class UserAuthenticationResponse {
    private final String token;
    private final Long expirationDate;
    private final String roles;

    public UserAuthenticationResponse(TokenDto token, String role) {
        this.token = token.getToken();
        this.expirationDate = token.getExpirationDate();
        this.roles = role;
    }
}
