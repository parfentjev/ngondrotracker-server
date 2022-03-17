package com.ngondrotracker.meditation.controller;

import com.ngondrotracker.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.common.exception.ItemDoesNotExist;
import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.service.MeditationService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.Map;

import static com.ngondrotracker.common.util.mapper.TestUtils.jsonMapper;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MeditationController.class)
public class MeditationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeditationService meditationService;

    @Test
    @WithMockUser(username = "username", roles = "ADMIN")
    public void createMeditation() throws Exception {
        String title = "newTitle";
        String path = "newPath";
        String goal = "111111";
        int intGoal = 111111;

        when(meditationService.create(title, path, intGoal)).thenReturn(new MeditationDto(title, path, intGoal));

        MockHttpServletRequestBuilder request = post("/meditation/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", title, "path", path, "goal", goal)));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(true)));
    }

    @Test
    @WithMockUser(username = "username", roles = "ADMIN")
    public void createMeditationThatAlreadyExists() throws Exception {
        String title = "newTitle";
        String path = "newPath";
        String goal = "111111";
        int intGoal = 111111;

        when(meditationService.create(title, path, intGoal)).thenThrow(new ItemAlreadyExistsException());

        MockHttpServletRequestBuilder request = post("/meditation/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", title, "path", path, "goal", goal)));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("message", is("ALREADY_EXISTS")));
    }

    @Test
    @WithMockUser(username = "username", roles = "USER")
    public void createMeditationUser() throws Exception {
        MockHttpServletRequestBuilder request = post("/meditation/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", "title", "path", "path", "goal", "goal")));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "username", roles = "NOT_VERIFIED")
    public void createMeditationNotVerified() throws Exception {
        MockHttpServletRequestBuilder request = post("/meditation/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", "title", "path", "path", "goal", "goal")));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithAnonymousUser
    public void createMeditationAnonymous() throws Exception {
        MockHttpServletRequestBuilder request = post("/meditation/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", "title", "path", "path", "goal", "goal")));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithAnonymousUser
    public void getMeditationByMath() throws Exception {
        String title = "newTitle";
        String path = "newPath";
        int intGoal = 111111;

        when(meditationService.getByPath(path)).thenReturn(new MeditationDto(title, path, intGoal));

        MockHttpServletRequestBuilder request = get("/meditation/get")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("path", path)));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("result.title", is(title)))
                .andExpect(jsonPath("result.path", is(path)))
                .andExpect(jsonPath("result.goal", is(intGoal)));
    }

    @Test
    @WithAnonymousUser
    public void getMeditationByMathDoesNotExist() throws Exception {
        String path = "newPath";

        when(meditationService.getByPath(path)).thenThrow(new ItemDoesNotExist());

        MockHttpServletRequestBuilder request = get("/meditation/get")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("path", path)));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message", is("DOES_NOT_EXIST")));
    }

    @Test
    @WithAnonymousUser
    public void getAll() throws Exception {
        MeditationDto meditationDto1 = new MeditationDto("m1", "p1", 1);
        MeditationDto meditationDto2 = new MeditationDto("m2", "p2", 2);

        when(meditationService.findAll()).thenReturn(Arrays.asList(meditationDto1, meditationDto2));

        MockHttpServletRequestBuilder request = get("/meditation/getAll")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(2)))
                .andExpect(jsonPath("$.result[0]", hasEntry("title", "m1")))
                .andExpect(jsonPath("$.result[0]", hasEntry("path", "p1")))
                .andExpect(jsonPath("$.result[0]", hasEntry("goal", 1)))
                .andExpect(jsonPath("$.result[1]", hasEntry("title", "m2")))
                .andExpect(jsonPath("$.result[1]", hasEntry("path", "p2")))
                .andExpect(jsonPath("$.result[1]", hasEntry("goal", 2)));
    }

    @Test
    @WithAnonymousUser
    public void getAllEmpty() throws Exception {
        when(meditationService.findAll()).thenReturn(Lists.emptyList());

        MockHttpServletRequestBuilder request = get("/meditation/getAll")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(0)));
    }
}
