package com.ngondrotracker.meditation.dto;

import lombok.Data;

@Data
public class MeditationDto {
    private String title;

    private String id;

    private Integer goal;

    private Integer order;
}
