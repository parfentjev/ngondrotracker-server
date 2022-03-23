package com.ngondrotracker.user.service.impl;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.token.dto.TokenDto;
import com.ngondrotracker.token.service.TokenService;
import com.ngondrotracker.user.service.UserAuthenticationService;
import com.ngondrotracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@ComponentScan(basePackages = {"com.ngondrotracker.application"})
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public TokenDto signup(String username, String password) throws ResourceAlreadyExistsException {
        userService.create(username, passwordEncoder.encode(password));

        return authenticate(username, password);
    }

    @Override
    public TokenDto signin(String username, String password) {
        return authenticate(username, password);
    }

    private TokenDto authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenService.generateToken(authentication.getName());
    }
}

