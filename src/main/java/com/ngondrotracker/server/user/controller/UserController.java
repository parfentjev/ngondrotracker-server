package com.ngondrotracker.server.user.controller;

import com.ngondrotracker.server.common.controller.AbstractRestController;
import com.ngondrotracker.server.common.response.ResultResponse;
import com.ngondrotracker.server.common.support.factory.ResultResponseFactory;
import com.ngondrotracker.server.user.controller.request.SignInRequest;
import com.ngondrotracker.server.user.controller.request.SignUpRequest;
import com.ngondrotracker.server.user.exception.AuthenticationException;
import com.ngondrotracker.server.user.model.UserTokenDto;
import com.ngondrotracker.server.user.service.interfaces.UserAuthenticationService;
import com.ngondrotracker.server.user.service.interfaces.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractRestController {
    @Autowired
    private UserAuthenticationService authenticationService;

    @Autowired
    private UserTokenService userTokenService;

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<UserTokenDto>> signup(@RequestBody @Valid SignUpRequest signupRequest) {
        ResultResponse<UserTokenDto> response;

        try {
            UserTokenDto token = authenticationService.signup(signupRequest.getEmail(), signupRequest.getPassword());
            response = new ResultResponseFactory<UserTokenDto>().successful(token);
        } catch (AuthenticationException e) {
            response = new ResultResponseFactory<UserTokenDto>().notSuccessful(e.getMessage());
        }

        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @PostMapping(path = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<UserTokenDto>> signin(@RequestBody @Valid SignInRequest signinRequest) {
        UserTokenDto token = authenticationService.signin(signinRequest.getEmail(), signinRequest.getPassword());
        ResultResponse<UserTokenDto> response = new ResultResponseFactory<UserTokenDto>().successful(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(HAS_ANY_ROLE)
    public ResponseEntity<ResultResponse<UserTokenDto>> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        String currentToken = authorizationHeader.substring(7);
        UserTokenDto token = userTokenService.refreshToken(currentToken);
        ResultResponse<UserTokenDto> response = new ResultResponseFactory<UserTokenDto>().successful(token);

        return ResponseEntity.ok(response);
    }
}
