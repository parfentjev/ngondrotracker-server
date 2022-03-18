package com.ngondrotracker.user.controller;

import com.ngondrotracker.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.token.dto.TokenDto;
import com.ngondrotracker.token.service.TokenService;
import com.ngondrotracker.user.service.UserAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static com.ngondrotracker.common.util.mapper.TestUtils.jsonMapper;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthenticationService userAuthenticationService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserDetailsService userDetailsService;

    @InjectMocks
    private UserController userController;

    private final String email = "tester-name";
    private final String password = "tester-password";

    @Test
    @WithAnonymousUser
    public void signUpSuccess() throws Exception {
        when(userAuthenticationService.signup(any(), any()))
                .thenReturn(new TokenDto("generatedToken", 123L));

        when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(User.withUsername("tester").password("tester").roles("TESTER").build());

        MockHttpServletRequestBuilder request = post("/user/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("success", is(true)))
                .andExpect(jsonPath("result.token", is("generatedToken")))
                .andExpect(jsonPath("result.expirationDate", is(123)))
                .andExpect(jsonPath("result.roles", is("ROLE_TESTER")));
    }

    @Test
    @WithAnonymousUser
    public void signInSuccess() throws Exception {
        when(userAuthenticationService.signin(any(), any()))
                .thenReturn(new TokenDto("generatedToken", 123L));

        when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(User.withUsername("tester").password("tester").roles("TESTER").build());

        MockHttpServletRequestBuilder request = post("/user/signin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(true)))
                .andExpect(jsonPath("result.token", is("generatedToken")))
                .andExpect(jsonPath("result.expirationDate", is(123)))
                .andExpect(jsonPath("result.roles", is("ROLE_TESTER")));
    }

    @Test
    @WithAnonymousUser
    public void signInUserAlreadyExists() throws Exception {
        when(userAuthenticationService.signup(any(), any()))
                .thenThrow(new ItemAlreadyExistsException());

        MockHttpServletRequestBuilder request = post("/user/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("email", email, "password", password)));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("message", is("ALREADY_EXISTS")));
    }

    @Test
    @WithAnonymousUser
    public void signUpFailNoUsername() throws Exception {
        MockHttpServletRequestBuilder request = post("/user/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("email", "", "password", password)));

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @Test
    @WithAnonymousUser
    public void signUpFailNoPassword() throws Exception {
        MockHttpServletRequestBuilder request = post("/user/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("email", email, "password", "")));

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "username", roles = "NOT_VERIFIED")
    public void refreshToken() throws Exception {
        when(tokenService.refreshToken(any()))
                .thenReturn(new TokenDto("generatedToken", 123L));

        when(tokenService.isValid(any()))
                .thenReturn(true);

        MockHttpServletRequestBuilder request = get("/user/refreshToken")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(true)))
                .andExpect(jsonPath("result.token", is("generatedToken")))
                .andExpect(jsonPath("result.expirationDate", is(123)));
    }

    @Test
    @WithAnonymousUser
    public void refreshTokenNoRoles() throws Exception {
        when(tokenService.refreshToken(any()))
                .thenReturn(new TokenDto("generatedToken", 123L));

        when(tokenService.isValid(any()))
                .thenReturn(true);

        MockHttpServletRequestBuilder request = get("/user/refreshToken")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }
}
