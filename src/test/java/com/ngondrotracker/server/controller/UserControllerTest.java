package com.ngondrotracker.server.controller;

import com.ngondrotracker.server.user.exception.AuthenticationException;
import com.ngondrotracker.server.user.model.UserTokenDto;
import com.ngondrotracker.server.user.service.interfaces.UserAuthenticationService;
import com.ngondrotracker.server.user.service.interfaces.UserTokenService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthenticationService authenticationService;

    @MockBean
    private UserTokenService tokenService;

    @MockBean
    private UserDetailsService userDetailsService;

    private final String email = "tester-name";
    private final String password = "tester-password";

    @Test
    @DisplayName("POST /user/signup success")
    public void signUpSuccess() throws Exception {
        Mockito.when(authenticationService.signup(any(), any()))
                .thenReturn(new UserTokenDto("generatedToken", 123L));

        Mockito.when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(User.withUsername("tester").password("tester").roles("TESTER").build());

        MockHttpServletRequestBuilder request = post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is(true)))
                .andExpect(jsonPath("result.token", Matchers.is("generatedToken")))
                .andExpect(jsonPath("result.expirationDate", Matchers.is(123)))
                .andExpect(jsonPath("result.roles", Matchers.is("ROLE_TESTER")));
    }

    @Test
    @DisplayName("POST /user/signin success")
    public void signInSuccess() throws Exception {
        Mockito.when(authenticationService.signin(any(), any()))
                .thenReturn(new UserTokenDto("generatedToken", 123L));

        Mockito.when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(User.withUsername("tester").password("tester").roles("TESTER").build());

        MockHttpServletRequestBuilder request = post("/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is(true)))
                .andExpect(jsonPath("result.token", Matchers.is("generatedToken")))
                .andExpect(jsonPath("result.expirationDate", Matchers.is(123)))
                .andExpect(jsonPath("result.roles", Matchers.is("ROLE_TESTER")));
    }

    @Test
    @DisplayName("POST /user/signup user already exists")
    public void signInUserAlreadyExists() throws Exception {
        Mockito.when(authenticationService.signup(any(), any()))
                .thenThrow(new AuthenticationException("ALREADY_EXISTS"));

        MockHttpServletRequestBuilder request = post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", Matchers.is(false)))
                .andExpect(jsonPath("message", Matchers.is("ALREADY_EXISTS")));
    }

    @Test
    @DisplayName("POST /user/signup no username")
    public void signUpFailNoUsername() throws Exception {
        MockHttpServletRequestBuilder request = post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", "", "password", password)));

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /user/signup no password")
    public void signUpFailNoPassword() throws Exception {
        MockHttpServletRequestBuilder request = post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", email, "password", "")));

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /user/refreshToken positive")
    public void refreshToken() throws Exception {
        Mockito.when(tokenService.refreshToken(any()))
                .thenReturn(new UserTokenDto("generatedToken", 123L));

        Mockito.when(tokenService.isValid(any()))
                .thenReturn(true);

        Mockito.when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(User.withUsername("username").password("password").roles("NOT_VERIFIED").build());

        MockHttpServletRequestBuilder request = get("/user/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is(true)))
                .andExpect(jsonPath("result.token", Matchers.is("generatedToken")))
                .andExpect(jsonPath("result.expirationDate", Matchers.is(123)));
    }

    @Test
    @DisplayName("GET /user/refreshToken has no roles")
    public void refreshTokenNoRoles() throws Exception {
        Mockito.when(tokenService.refreshToken(any()))
                .thenReturn(new UserTokenDto("generatedToken", 123L));

        Mockito.when(tokenService.isValid(any()))
                .thenReturn(true);

        Mockito.when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(User.withUsername("username").password("password").roles().build());

        MockHttpServletRequestBuilder request = get("/user/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }
}
