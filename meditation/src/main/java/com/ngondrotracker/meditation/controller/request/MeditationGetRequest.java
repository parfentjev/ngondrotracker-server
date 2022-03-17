package com.ngondrotracker.meditation.controller.request;

import javax.validation.constraints.NotBlank;

public class MeditationGetRequest {
    @NotBlank
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
