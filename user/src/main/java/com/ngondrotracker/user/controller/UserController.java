package com.ngondrotracker.user.controller;

import com.ngondrotracker.common.controller.AbstractRestController;
import com.ngondrotracker.common.response.ResultResponse;
import com.ngondrotracker.common.util.factory.ResultResponseFactory;
import com.ngondrotracker.token.dto.TokenDto;
import com.ngondrotracker.token.service.TokenService;
import com.ngondrotracker.user.controller.request.UserSignInRequest;
import com.ngondrotracker.user.controller.request.UserSignUpRequest;
import com.ngondrotracker.user.controller.response.UserAuthenticationResponse;
import com.ngondrotracker.user.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractRestController {
    @Autowired
    private UserAuthenticationService authenticationService;

    @Autowired
    private TokenService userTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping(path = "/signup", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<UserAuthenticationResponse>> signup(@RequestBody @Valid UserSignUpRequest request) {
        TokenDto token = authenticationService.signup(request.getEmail(), request.getPassword());

        String roles = userDetailsService.loadUserByUsername(request.getEmail())
                .getAuthorities()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        ResultResponse<UserAuthenticationResponse> response = new ResultResponseFactory<UserAuthenticationResponse>().successful(new UserAuthenticationResponse(token, roles));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/signin", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<UserAuthenticationResponse>> signin(@RequestBody @Valid UserSignInRequest request) {
        TokenDto token = authenticationService.signin(request.getEmail(), request.getPassword());

        String roles = userDetailsService.loadUserByUsername(request.getEmail())
                .getAuthorities()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        ResultResponse<UserAuthenticationResponse> response = new ResultResponseFactory<UserAuthenticationResponse>()
                .successful(new UserAuthenticationResponse(token, roles));

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/refreshToken", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(ANY_ROLE)
    public ResponseEntity<ResultResponse<TokenDto>> refreshToken(@RequestHeader("Authorization") String header) {
        String currentToken = header.substring(7);
        TokenDto token = userTokenService.refreshToken(currentToken);
        ResultResponse<TokenDto> response = new ResultResponseFactory<TokenDto>().successful(token);

        return ResponseEntity.ok(response);
    }
}
