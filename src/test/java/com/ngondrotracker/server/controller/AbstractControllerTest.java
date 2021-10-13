package com.ngondrotracker.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngondrotracker.server.Application;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test"})
public abstract class AbstractControllerTest {
    protected String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
