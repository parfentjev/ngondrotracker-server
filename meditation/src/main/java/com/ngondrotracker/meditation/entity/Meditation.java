package com.ngondrotracker.meditation.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "path", name = "uniquePathConstraint")
})
public class Meditation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "goal", nullable = false)
    private Integer goal;

    @Column(name = "sorting_order", nullable = false)
    private Integer order;
}
