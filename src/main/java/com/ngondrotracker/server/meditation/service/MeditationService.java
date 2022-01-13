package com.ngondrotracker.server.meditation.service;

import com.ngondrotracker.server.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.server.common.exception.ItemDoesNotExist;
import com.ngondrotracker.server.meditation.model.MeditationDto;

import java.util.List;

public interface MeditationService {
    void create(String title, String path, int goal) throws ItemAlreadyExistsException;

    MeditationDto getByPath(String path) throws ItemDoesNotExist;

    List<MeditationDto> getAll();
}
