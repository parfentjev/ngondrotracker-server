package com.ngondrotracker.server.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

public class UserControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final String email = "tester-name";
    private final String password = "tester-password";

    @Test
    @Order(1)
    @DisplayName("POST /user/signup success")
    public void signUpSuccess() throws Exception {
        String body = asJsonString(Map.of("email", email, "password", password));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("result.token", Matchers.is(Matchers.not(Matchers.empty()))))
                .andExpect(MockMvcResultMatchers.jsonPath("result.expirationDate", Matchers.is(Matchers.not(Matchers.empty()))));
    }

    @Test
    @Order(2)
    @DisplayName("POST /user/signin success")
    public void signInSuccess() throws Exception {
        String body = asJsonString(Map.of("email", email, "password", password));

        MockHttpServletRequestBuilder singInRequest = MockMvcRequestBuilders.post("/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        mockMvc.perform(singInRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("result.token", Matchers.is(Matchers.not(Matchers.empty()))))
                .andExpect(MockMvcResultMatchers.jsonPath("result.expirationDate", Matchers.is(Matchers.not(Matchers.empty()))));
    }

    @Test
    @Order(3)
    @DisplayName("POST /user/signup user already exists")
    public void signInUserAlreadyExists() throws Exception {
        String body = asJsonString(Map.of("email", email, "password", password));

        MockHttpServletRequestBuilder singInRequest = MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        mockMvc.perform(singInRequest)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("success", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.is("ALREADY_EXISTS")));
    }

    @Test
    @DisplayName("POST /user/signup no username")
    public void signUpFailNoUsername() throws Exception {
        String body = asJsonString(Map.of("email", "", "password", password));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /user/signup no password")
    public void signUpFailNoPassword() throws Exception {
        String body = asJsonString(Map.of("email", email, "password", ""));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /user/signin wrong username")
    public void signInWrongUsername() throws Exception {
        String body = asJsonString(Map.of("email", "blablabla", "password", password));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    @DisplayName("POST /user/signin wrong password")
    public void signInWrongPassword() throws Exception {
        String body = asJsonString(Map.of("email", email, "password", "blablabla"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/user/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
}
