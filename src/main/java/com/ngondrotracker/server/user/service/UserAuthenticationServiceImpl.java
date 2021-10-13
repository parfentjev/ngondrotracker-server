package com.ngondrotracker.server.user.service;

import com.ngondrotracker.server.user.exception.AuthenticationException;
import com.ngondrotracker.server.user.model.UserTokenDto;
import com.ngondrotracker.server.user.service.interfaces.UserAuthenticationService;
import com.ngondrotracker.server.user.service.interfaces.UserService;
import com.ngondrotracker.server.user.service.interfaces.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserTokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserTokenDto signup(String username, String password) {
        if (userService.exists(username))
            throw new AuthenticationException("ALREADY_EXISTS");

        userService.create(username, passwordEncoder.encode(password));

        return authenticate(username, password);
    }

    @Override
    public UserTokenDto signin(String username, String password) {
        return authenticate(username, password);
    }

    private UserTokenDto authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenService.generateToken(authentication.getName());
    }
}

