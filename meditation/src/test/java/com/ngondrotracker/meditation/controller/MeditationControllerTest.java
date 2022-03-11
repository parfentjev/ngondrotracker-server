package com.ngondrotracker.meditation.controller;

import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.service.MeditationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static com.ngondrotracker.common.util.mapper.TestUtils.jsonMapper;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    public void createMeditationThatAlreadyExists() {

    }

    @Test
    public void createMeditationUser() {

    }

    @Test
    public void createMeditationNotVerified() {

    }

    @Test
    public void createMeditationAnonymous() {

    }

    @Test
    public void getMeditationByMath() {

    }

    @Test
    public void getMeditationByMathDoesNotExist() {

    }

    @Test
    public void getAll() {

    }

    @Test
    public void getAllEmpty() {

    }
}
