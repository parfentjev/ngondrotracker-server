package com.ngondrotracker.meditation.util.mapper;

import com.ngondrotracker.meditation.controller.request.MeditationCreateRequest;
import com.ngondrotracker.meditation.entity.Meditation;
import com.ngondrotracker.meditation.dto.MeditationDto;

public class MeditationMapper {
    public MeditationDto entityToDto(Meditation meditation) {
        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setTitle(meditation.getTitle());
        meditationDto.setPath(meditation.getPath());
        meditationDto.setGoal(meditation.getGoal());
        meditationDto.setOrder(meditation.getOrder());

        return meditationDto;
    }

    public Meditation dtoToEntity(MeditationDto meditationDto) {
        Meditation meditation = new Meditation();
        meditation.setTitle(meditationDto.getTitle());
        meditation.setPath(meditationDto.getPath());
        meditation.setGoal(meditationDto.getGoal());
        meditation.setOrder(meditationDto.getOrder());

        return meditation;
    }

    public MeditationDto createRequestToDto(MeditationCreateRequest request) {
        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setTitle(request.getTitle());
        meditationDto.setPath(request.getPath());
        meditationDto.setGoal(request.getGoal());
        meditationDto.setOrder(request.getOrder());

        return meditationDto;
    }
}
