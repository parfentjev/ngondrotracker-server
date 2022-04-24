package com.ngondrotracker.meditation.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PracticeSummaryDto {
    private MeditationDto meditationDto;

    private Integer repetitions;

    private PracticeResultDto latestPracticeResult;

    private LocalDate estimatedCompletionDate;
}
