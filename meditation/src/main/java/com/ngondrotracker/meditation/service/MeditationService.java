package com.ngondrotracker.meditation.service;

import com.ngondrotracker.meditation.dto.MeditationDto;

import java.util.List;

public interface MeditationService {
    MeditationDto create(MeditationDto meditationDto);

    MeditationDto getByPath(String path);

    MeditationDto getById(long id);

    List<MeditationDto> findAll();
}
