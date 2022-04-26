package com.ngondrotracker.meditation.service;

import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.dto.PracticeResultDto;
import com.ngondrotracker.meditation.dto.PracticeSummaryDto;
import com.ngondrotracker.user.dto.UserDto;

import java.util.List;

public interface PracticeResultService {
    PracticeResultDto addResult(PracticeResultDto practiceResultDto);

    void deleteResult(PracticeResultDto practiceResultDto);

    List<PracticeResultDto> getResults(UserDto userDto, MeditationDto meditationDto);

    PracticeSummaryDto getSummary(UserDto userDto, MeditationDto meditationDto);
}
