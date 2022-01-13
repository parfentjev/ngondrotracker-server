package com.ngondrotracker.server.meditation.controller.request;

import javax.validation.constraints.NotBlank;

public class MeditationGetRequest {
    @NotBlank
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
