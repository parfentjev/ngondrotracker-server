package com.ngondrotracker.meditation.service.impl;

import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.dto.PracticeResultDto;
import com.ngondrotracker.meditation.repository.PracticeResultRepository;
import com.ngondrotracker.meditation.service.PracticeResultService;
import com.ngondrotracker.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PracticeResultServiceImpl implements PracticeResultService {
    @Autowired
    private PracticeResultRepository resultRepository;

    @Override
    public PracticeResultDto addResult(PracticeResultDto practiceResultDto) {
        return null;
    }

    @Override
    public void deleteResult(PracticeResultDto practiceResultDto) {

    }

    @Override
    public List<PracticeResultDto> getResultByUserAndMeditation(UserDto userDto, MeditationDto meditationDto) {
        return null;
    }

    @Override
    public Integer getSummaryByUserAndMeditation(UserDto userDto, MeditationDto meditationDto) {
        return null;
    }
}
