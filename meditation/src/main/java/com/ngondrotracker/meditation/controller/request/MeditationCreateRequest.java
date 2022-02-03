package com.ngondrotracker.meditation.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MeditationCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String path;

    @NotNull
    private Integer goal;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }
}
