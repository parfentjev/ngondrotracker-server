package com.ngondrotracker.meditation.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class Meditation {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "goal", nullable = false)
    private Integer goal;

    @Column(name = "order", nullable = false)
    private Integer order;
}
