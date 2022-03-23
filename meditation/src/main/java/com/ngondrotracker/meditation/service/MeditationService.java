package com.ngondrotracker.meditation.service;

import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.meditation.dto.MeditationDto;

import java.util.List;

public interface MeditationService {
    MeditationDto create(String title, String path, int goal);

    MeditationDto getByPath(String path) throws ResourceNotFoundException;

    List<MeditationDto> findAll();
}
