package com.ngondrotracker.meditation.service;

import com.ngondrotracker.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.common.exception.ItemDoesNotExist;
import com.ngondrotracker.meditation.dto.MeditationDto;

import java.util.List;

public interface MeditationService {
    void create(String title, String path, int goal) throws ItemAlreadyExistsException;

    MeditationDto getByPath(String path) throws ItemDoesNotExist;

    List<MeditationDto> getAll();
}
