package com.ngondrotracker.meditation.dto;

import lombok.Data;

import java.util.TimeZone;

@Data
public class PracticeResultDto {
    private Long id;

    private Long timestamp;

    private TimeZone timeZone;

    private Integer repetitions;

    private MeditationDto meditation;
}
