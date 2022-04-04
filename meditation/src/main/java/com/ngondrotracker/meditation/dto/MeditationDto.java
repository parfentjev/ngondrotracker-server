package com.ngondrotracker.meditation.dto;

import lombok.Data;

@Data
public class MeditationDto {
    private Long id;

    private String title;

    private String path;

    private Integer goal;

    private Integer order;
}
