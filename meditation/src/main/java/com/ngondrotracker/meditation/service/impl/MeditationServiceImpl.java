package com.ngondrotracker.meditation.service.impl;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.meditation.entity.Meditation;
import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.repository.MeditationRepository;
import com.ngondrotracker.meditation.service.MeditationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ngondrotracker.meditation.util.MeditationUtils.meditationMapper;

@Service
public class MeditationServiceImpl implements MeditationService {
    @Autowired
    private MeditationRepository repository;

    @Override
    public MeditationDto create(String title, String path, int goal) {
        if (repository.findByPath(path).isPresent()) throw new ResourceAlreadyExistsException("Meditation");

        Meditation meditation = new Meditation();
        meditation.setTitle(title);
        meditation.setPath(path);
        meditation.setGoal(goal);

        repository.save(meditation);

        return meditationMapper().entityToDto(meditation);
    }

    @Override
    public MeditationDto getByPath(String path) throws ResourceNotFoundException {
        Meditation meditation = repository.findByPath(path).orElseThrow(() -> new ResourceNotFoundException("Meditation", "path", path));

        return meditationMapper().entityToDto(meditation);
    }

    @Override
    public List<MeditationDto> findAll() {
        return repository.findAll().stream().map(meditation -> meditationMapper().entityToDto(meditation)).collect(Collectors.toList());
    }
}
