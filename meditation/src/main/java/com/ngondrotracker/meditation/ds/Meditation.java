package com.ngondrotracker.meditation.ds;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Meditation {
    private String title;

    @Id
    private String path;

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
