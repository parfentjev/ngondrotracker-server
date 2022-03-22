package com.ngondrotracker.meditation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeditationDto {
    private String title;

    private String path;

    private Integer goal;
}
