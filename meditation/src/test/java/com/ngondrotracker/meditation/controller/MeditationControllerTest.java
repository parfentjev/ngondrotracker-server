package com.ngondrotracker.meditation.controller;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.common.exception.ResourceNotFoundException;
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
        int goal = 111111;
        int order = 1;

        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setTitle(title);
        meditationDto.setId(path);
        meditationDto.setGoal(goal);
        meditationDto.setOrder(order);

        when(meditationService.create(meditationDto)).thenReturn(meditationDto);

        MockHttpServletRequestBuilder request = post("/meditations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", title,
                        "path", path,
                        "goal", String.valueOf(goal),
                        "order", String.valueOf(order))));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("success", is(true)))
                .andExpect(jsonPath("result.title", is(title)))
                .andExpect(jsonPath("result.path", is(path)))
                .andExpect(jsonPath("result.goal", is(goal)));
    }

    @Test
    @WithMockUser(username = "username", roles = "ADMIN")
    public void createMeditationThatAlreadyExists() throws Exception {
        String title = "newTitle";
        String path = "newPath";
        int goal = 111111;
        int order = 1;

        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setTitle(title);
        meditationDto.setId(path);
        meditationDto.setGoal(goal);
        meditationDto.setOrder(order);

        when(meditationService.create(meditationDto)).thenThrow(new ResourceAlreadyExistsException("Meditation"));

        MockHttpServletRequestBuilder request = post("/meditations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", title,
                        "path", path,
                        "goal", String.valueOf(goal),
                        "order", String.valueOf(order))));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("message", is("Meditation already exists")));
    }

    @Test
    @WithMockUser(username = "username", roles = "USER")
    public void createMeditationUser() throws Exception {
        MockHttpServletRequestBuilder request = post("/meditations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", "title",
                        "path", "path",
                        "goal", "1",
                        "order", "1")));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("message", is("Access denied")));
    }

    @Test
    @WithMockUser(username = "username", roles = "NOT_VERIFIED")
    public void createMeditationNotVerified() throws Exception {
        MockHttpServletRequestBuilder request = post("/meditations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", "title",
                        "path", "path",
                        "goal", "1",
                        "order", "1")));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("message", is("Access denied")));
    }

    @Test
    @WithAnonymousUser
    public void createMeditationAnonymous() throws Exception {
        MockHttpServletRequestBuilder request = post("/meditations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper().mapToJsonString(Map.of("title", "title",
                        "path", "path",
                        "goal", "1",
                        "order", "1")));

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("message", is("Access denied")));
    }

    @Test
    @WithAnonymousUser
    public void getMeditationByPath() throws Exception {
        String title = "newTitle";
        String path = "newPath";
        int goal = 111111;
        int order = 1;

        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setTitle(title);
        meditationDto.setId(path);
        meditationDto.setGoal(goal);
        meditationDto.setOrder(order);

        when(meditationService.getByPath(path)).thenReturn(meditationDto);

        MockHttpServletRequestBuilder request = get("/meditations/" + path);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("result.title", is(title)))
                .andExpect(jsonPath("result.path", is(path)))
                .andExpect(jsonPath("result.goal", is(goal)))
                .andExpect(jsonPath("result.order", is(order)));
    }

    @Test
    @WithAnonymousUser
    public void getMeditationByMathDoesNotExist() throws Exception {
        String path = "newPath";

        when(meditationService.getByPath(path)).thenThrow(new ResourceNotFoundException("Meditation", "path", "x"));

        MockHttpServletRequestBuilder request = get("/meditations/" + path);

        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("message", is("Meditation not found, where path = x")));
    }

    @Test
    @WithAnonymousUser
    public void getMeditations() throws Exception {

        MeditationDto meditationDto1 = new MeditationDto();
        meditationDto1.setTitle("m1");
        meditationDto1.setId("p1");
        meditationDto1.setGoal(1);
        meditationDto1.setOrder(1);

        MeditationDto meditationDto2 = new MeditationDto();
        meditationDto2.setTitle("m2");
        meditationDto2.setId("p2");
        meditationDto2.setGoal(2);
        meditationDto2.setOrder(2);

        when(meditationService.findAll()).thenReturn(Arrays.asList(meditationDto1, meditationDto2));

        MockHttpServletRequestBuilder request = get("/meditations/");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(2)))
                .andExpect(jsonPath("$.result[0]", hasEntry("title", "m1")))
                .andExpect(jsonPath("$.result[0]", hasEntry("path", "p1")))
                .andExpect(jsonPath("$.result[0]", hasEntry("goal", 1)))
                .andExpect(jsonPath("$.result[0]", hasEntry("order", 1)))
                .andExpect(jsonPath("$.result[1]", hasEntry("title", "m2")))
                .andExpect(jsonPath("$.result[1]", hasEntry("path", "p2")))
                .andExpect(jsonPath("$.result[1]", hasEntry("goal", 2)))
                .andExpect(jsonPath("$.result[1]", hasEntry("order", 2)));
    }

    @Test
    @WithAnonymousUser
    public void getMeditationsEmptyList() throws Exception {
        when(meditationService.findAll()).thenReturn(Lists.emptyList());

        MockHttpServletRequestBuilder request = get("/meditations/");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(0)));
    }
}
