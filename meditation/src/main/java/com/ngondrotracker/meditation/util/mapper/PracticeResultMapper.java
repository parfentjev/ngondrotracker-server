package com.ngondrotracker.meditation.util.mapper;

import com.ngondrotracker.meditation.dto.PracticeResultDto;
import com.ngondrotracker.meditation.entity.PracticeResult;

import static com.ngondrotracker.meditation.util.MeditationUtils.meditationMapper;
import static com.ngondrotracker.user.util.UserUtils.userMapper;

public class PracticeResultMapper {
    public PracticeResult dtoToEntity(PracticeResultDto practiceResultDto) {
        PracticeResult practiceResult = new PracticeResult();
        practiceResult.setId(practiceResultDto.getId());
        practiceResult.setTimestamp(practiceResultDto.getTimestamp());
        practiceResult.setTimeZone(practiceResultDto.getTimeZone());
        practiceResult.setRepetitions(practiceResultDto.getRepetitions());
        practiceResult.setUser(userMapper().dtoToEntity(practiceResultDto.getUserDto()));
        practiceResult.setMeditation(meditationMapper().dtoToEntity(practiceResultDto.getMeditationDto()));

        return practiceResult;
    }

    public PracticeResultDto entityToDto(PracticeResult practiceResult) {
        PracticeResultDto practiceResultDto = new PracticeResultDto();
        practiceResultDto.setId(practiceResult.getId());
        practiceResultDto.setTimestamp(practiceResult.getTimestamp());
        practiceResultDto.setTimeZone(practiceResult.getTimeZone());
        practiceResultDto.setRepetitions(practiceResult.getRepetitions());
        practiceResultDto.setUserDto(userMapper().entityToDto(practiceResult.getUser()));
        practiceResultDto.setMeditationDto(meditationMapper().entityToDto(practiceResult.getMeditation()));

        return practiceResultDto;
    }
}
