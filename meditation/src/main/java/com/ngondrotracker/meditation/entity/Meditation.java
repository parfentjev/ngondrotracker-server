package com.ngondrotracker.meditation.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Meditation {
    @Column(name = "title", nullable = false)
    private String title;

    @Id
    @Column(name = "path", nullable = false)
    private String id;

    @Column(name = "goal", nullable = false)
    private Integer goal;

    @Column(name = "sorting_order", nullable = false)
    private Integer order;
}
