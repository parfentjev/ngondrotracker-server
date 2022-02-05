package com.ngondrotracker.meditation.util.mapper;

import com.ngondrotracker.meditation.entity.Meditation;
import com.ngondrotracker.meditation.dto.MeditationDto;

public class MeditationMapper {
    public MeditationDto entityToDto(Meditation meditation) {
        return new MeditationDto(meditation.getTitle(), meditation.getPath(), meditation.getGoal());
    }
}
