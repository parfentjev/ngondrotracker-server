package com.ngondrotracker.server.meditation.service;

import com.ngondrotracker.server.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.server.common.exception.ItemDoesNotExist;
import com.ngondrotracker.server.meditation.model.Meditation;
import com.ngondrotracker.server.meditation.model.MeditationDto;
import com.ngondrotracker.server.meditation.model.MeditationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ngondrotracker.server.common.mapper.DataObjectMapper.meditationMapper;

@Service
public class MeditationServiceImpl implements MeditationService {
    @Autowired
    private MeditationRepository repository;

    @Override
    public void create(String title, String path, int goal) throws ItemAlreadyExistsException {
        if (repository.findByPath(path).isPresent())
            throw new ItemAlreadyExistsException();

        Meditation meditation = new Meditation();
        meditation.setTitle(title);
        meditation.setPath(path);
        meditation.setGoal(goal);

        repository.save(meditation);
    }

    @Override
    public MeditationDto getByPath(String path) {
        Meditation meditation = repository.findByPath(path).orElseThrow(ItemDoesNotExist::new);

        return meditationMapper().meditationToDto(meditation);
    }

    @Override
    public List<MeditationDto> getAll() {
        return repository.findAll()
                .stream()
                .map(meditation -> meditationMapper().meditationToDto(meditation))
                .collect(Collectors.toList());
    }
}
