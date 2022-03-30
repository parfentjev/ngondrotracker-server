package com.ngondrotracker.meditation.service;

import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.dto.PracticeResultDto;
import com.ngondrotracker.user.dto.UserDto;

import java.util.List;

public interface PracticeResultService {
    PracticeResultDto addResult(PracticeResultDto practiceResultDto);

    void deleteResult(PracticeResultDto practiceResultDto);

    List<PracticeResultDto> getResultByUserAndMeditation(UserDto userDto, MeditationDto meditationDto);

    Integer getSummaryByUserAndMeditation(UserDto userDto, MeditationDto meditationDto);
}
