package com.ngondrotracker.server.common.mapper;

import com.ngondrotracker.server.meditation.model.Meditation;
import com.ngondrotracker.server.meditation.model.MeditationDto;

public class MeditationDataObjectMapper {
    MeditationDataObjectMapper() {
        // blank constructor
    }

    public MeditationDto meditationToDto(Meditation meditation) {
        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setTitle(meditation.getTitle());
        meditationDto.setPath(meditation.getPath());
        meditationDto.setGoal(meditation.getGoal());

        return meditationDto;
    }
}
