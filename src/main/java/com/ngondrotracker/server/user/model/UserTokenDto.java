package com.ngondrotracker.server.user.model;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "token")
public class UserTokenDto {
    private String token;
    private Long expirationDate;

    public UserTokenDto(String token, Long expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }
}
