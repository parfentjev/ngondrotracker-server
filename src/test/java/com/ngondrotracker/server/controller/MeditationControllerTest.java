package com.ngondrotracker.server.controller;

import com.ngondrotracker.server.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.server.common.exception.ItemDoesNotExist;
import com.ngondrotracker.server.meditation.model.MeditationDto;
import com.ngondrotracker.server.meditation.service.MeditationServiceImpl;
import com.ngondrotracker.server.user.service.interfaces.UserTokenService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MeditationControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeditationServiceImpl meditationService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserTokenService tokenService;

    @Test
    @DisplayName("POST /meditation/create - success")
    public void createMeditationSuccess() throws Exception {
        when(tokenService.isValid(any()))
                .thenReturn(true);

        when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(User.withUsername("username").password("password").roles("ADMIN").build());

        String content = asJsonString(Map.of(
                "title", "meditationTitle",
                "path", "meditationPath",
                "goal", 123
        ));

        MockHttpServletRequestBuilder request = post("/meditation/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is(true)));
    }

    @Test
    @DisplayName("POST /meditation/create - already exists")
    public void createMeditationAlreadyExists() throws Exception {
        when(tokenService.isValid(any()))
                .thenReturn(true);

        when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(User.withUsername("username").password("password").roles("ADMIN").build());

        String content = asJsonString(Map.of(
                "title", "meditationTitle",
                "path", "meditationPath",
                "goal", 123
        ));

        doThrow(new ItemAlreadyExistsException()).when(meditationService).create(any(), any(), anyInt());

        MockHttpServletRequestBuilder request = post("/meditation/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", Matchers.is(false)))
                .andExpect(jsonPath("message", Matchers.is("ALREADY_EXISTS")));
    }

    @Test
    @DisplayName("POST /meditation/create - no access")
    public void createMeditationNoAccess() throws Exception {
        String content = asJsonString(Map.of(
                "title", "meditationTitle",
                "path", "meditationPath",
                "goal", "123"
        ));

        MockHttpServletRequestBuilder request = post("/meditation/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", "Bearer currentToken");

        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET /meditation/getAll - success")
    public void getAllMeditations() throws Exception {
        List<MeditationDto> meditations = Arrays.asList(
                new MeditationDto("title1", "path1", 1),
                new MeditationDto("title2", "path2", 2)
        );

        when(meditationService.getAll()).thenReturn(meditations);

        MockHttpServletRequestBuilder request = get("/meditation/getAll");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is(true)))
                .andExpect(jsonPath("$.result[0].title", Matchers.is("title1")))
                .andExpect(jsonPath("$.result[0].path", Matchers.is("path1")))
                .andExpect(jsonPath("$.result[0].goal", Matchers.is(1)))
                .andExpect(jsonPath("$.result[1].title", Matchers.is("title2")))
                .andExpect(jsonPath("$.result[1].path", Matchers.is("path2")))
                .andExpect(jsonPath("$.result[1].goal", Matchers.is(2)));
    }

    @Test
    @DisplayName("GET /meditation/get - success")
    public void getMeditationByPath() throws Exception {
        MeditationDto meditationDto = new MeditationDto("title1", "path1", 1);
        when(meditationService.getByPath(any())).thenReturn(meditationDto);

        MockHttpServletRequestBuilder request = get("/meditation/get")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("id", "path1")));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Matchers.is(true)))
                .andExpect(jsonPath("result.title", Matchers.is("title1")))
                .andExpect(jsonPath("result.path", Matchers.is("path1")))
                .andExpect(jsonPath("result.goal", Matchers.is(1)));
    }

    @Test
    @DisplayName("GET /meditation/get - does not exist")
    public void getMeditationByPathDoesNotExist() throws Exception {
        when(meditationService.getByPath(any())).thenThrow(new ItemDoesNotExist());

        MockHttpServletRequestBuilder request = get("/meditation/get")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(Map.of("id", "path1")));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", Matchers.is(false)))
                .andExpect(jsonPath("message", Matchers.is("DOES_NOT_EXIST")));
    }
}
