package com.ngondrotracker.meditation.util.mapper;

import com.ngondrotracker.meditation.ds.Meditation;
import com.ngondrotracker.meditation.ds.MeditationDto;

public class MeditationMapper {
    public MeditationDto entityToDto(Meditation meditation) {
        return new MeditationDto(meditation.getTitle(), meditation.getPath(), meditation.getGoal());
    }
}
