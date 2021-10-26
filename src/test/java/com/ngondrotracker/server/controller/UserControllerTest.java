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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

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
        Mockito.when(authenticationService.signup(Mockito.any(), Mockito.any()))
                .thenReturn(new UserTokenDto("generatedToken", 123L));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("result.token", Matchers.is("generatedToken")))
                .andExpect(MockMvcResultMatchers.jsonPath("result.expirationDate", Matchers.is(123)));
    }

    @Test
    @DisplayName("POST /user/signin success")
    public void signInSuccess() throws Exception {
        Mockito.when(authenticationService.signin(Mockito.any(), Mockito.any()))
                .thenReturn(new UserTokenDto("generatedToken", 123L));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("result.token", Matchers.is("generatedToken")))
                .andExpect(MockMvcResultMatchers.jsonPath("result.expirationDate", Matchers.is(123)));
    }

    @Test
    @DisplayName("POST /user/signup user already exists")
    public void signInUserAlreadyExists() throws Exception {
        Mockito.when(authenticationService.signup(Mockito.any(), Mockito.any()))
                .thenThrow(new AuthenticationException("ALREADY_EXISTS"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("success", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.is("ALREADY_EXISTS")));
    }

    @Test
    @DisplayName("POST /user/signup no username")
    public void signUpFailNoUsername() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", "", "password", password)));

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /user/signup no password")
    public void signUpFailNoPassword() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("email", email, "password", "")));

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /user/refreshToken positive")
    public void refreshToken() throws Exception {
        Mockito.when(tokenService.refreshToken(Mockito.any()))
                .thenReturn(new UserTokenDto("generatedToken", 123L));

        Mockito.when(tokenService.isValid(Mockito.any()))
                .thenReturn(true);

        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any()))
                .thenReturn(User.withUsername("username").password("password").roles("NOT_VERIFIED").build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/user/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("result.token", Matchers.is("generatedToken")))
                .andExpect(MockMvcResultMatchers.jsonPath("result.expirationDate", Matchers.is(123)));
    }

    @Test
    @DisplayName("GET /user/refreshToken has no roles")
    public void refreshTokenNoRoles() throws Exception {
        Mockito.when(tokenService.refreshToken(Mockito.any()))
                .thenReturn(new UserTokenDto("generatedToken", 123L));

        Mockito.when(tokenService.isValid(Mockito.any()))
                .thenReturn(true);

        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any()))
                .thenReturn(User.withUsername("username").password("password").roles().build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/user/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
