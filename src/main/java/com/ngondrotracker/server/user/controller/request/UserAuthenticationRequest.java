package com.ngondrotracker.server.user.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserAuthenticationRequest {
    @NotBlank
    @Size(min = 3, max = 64)
    private String email;

    @NotBlank
    @Size(min = 6, max = 128)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
