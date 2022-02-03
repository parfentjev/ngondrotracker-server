package com.ngondrotracker.meditation.ds;

public class MeditationDto {
    private String title;

    private String path;

    private Integer goal;

    public MeditationDto(String title, String path, Integer goal) {
        this.title = title;
        this.path = path;
        this.goal = goal;
    }

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
