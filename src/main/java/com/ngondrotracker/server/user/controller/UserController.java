package com.ngondrotracker.server.user.controller;

import com.ngondrotracker.server.common.controller.AbstractRestController;
import com.ngondrotracker.server.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.server.common.response.ResultResponse;
import com.ngondrotracker.server.common.support.factory.ResultResponseFactory;
import com.ngondrotracker.server.user.controller.request.UserSignInRequest;
import com.ngondrotracker.server.user.controller.request.UserSignUpRequest;
import com.ngondrotracker.server.user.controller.response.UserAuthenticationResponse;
import com.ngondrotracker.server.user.model.UserTokenDto;
import com.ngondrotracker.server.user.service.interfaces.UserAuthenticationService;
import com.ngondrotracker.server.user.service.interfaces.UserTokenService;
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
    private UserTokenService userTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping(path = "/signup", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<UserAuthenticationResponse>> signup(@RequestBody @Valid UserSignUpRequest request) {
        ResultResponse<UserAuthenticationResponse> response;

        try {
            UserTokenDto token = authenticationService.signup(request.getEmail(), request.getPassword());

            String roles = userDetailsService.loadUserByUsername(request.getEmail())
                    .getAuthorities()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            response = new ResultResponseFactory<UserAuthenticationResponse>().successful(new UserAuthenticationResponse(token, roles));
        } catch (ItemAlreadyExistsException e) {
            response = new ResultResponseFactory<UserAuthenticationResponse>().notSuccessful(e.getMessage());
        }

        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @PostMapping(path = "/signin", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse<UserAuthenticationResponse>> signin(@RequestBody @Valid UserSignInRequest request) {
        UserTokenDto token = authenticationService.signin(request.getEmail(), request.getPassword());

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
    public ResponseEntity<ResultResponse<UserTokenDto>> refreshToken(@RequestHeader("Authorization") String header) {
        String currentToken = header.substring(7);
        UserTokenDto token = userTokenService.refreshToken(currentToken);
        ResultResponse<UserTokenDto> response = new ResultResponseFactory<UserTokenDto>().successful(token);

        return ResponseEntity.ok(response);
    }
}
