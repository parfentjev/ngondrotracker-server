package com.ngondrotracker.meditation.service;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.entity.Meditation;
import com.ngondrotracker.meditation.repository.MeditationRepository;
import com.ngondrotracker.meditation.service.impl.MeditationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeditationServiceImplTest {
    @Mock
    private MeditationRepository meditationRepository;

    @InjectMocks
    private MeditationServiceImpl meditationService;

    @Test
    public void createMeditation() throws ResourceAlreadyExistsException {
        String title = "newTitle";
        String path = "newPath";
        int intGoal = 111111;

        MeditationDto requestDto = new MeditationDto();
        requestDto.setTitle(title);
        requestDto.setPath(path);
        requestDto.setGoal(intGoal);

        MeditationDto meditationDto = meditationService.create(requestDto);
        verify(meditationRepository, times(1)).save(any());
        assertEquals(title, meditationDto.getTitle());
        assertEquals(path, meditationDto.getPath());
        assertEquals(intGoal, meditationDto.getGoal());
    }

    @Test
    public void createMeditationThatAlreadyExists() {
        when(meditationRepository.findByPath(any())).thenReturn(Optional.of(new Meditation()));
        assertThrows(ResourceAlreadyExistsException.class, () -> meditationService.create(new MeditationDto()));
        verify(meditationRepository, times(0)).save((any()));
    }

    @Test
    public void getMeditationByPath() {
        String title = "title";
        String path = "path";
        int goal = 100;

        Meditation meditation = new Meditation();
        meditation.setTitle(title);
        meditation.setPath(path);
        meditation.setGoal(goal);

        when(meditationRepository.findByPath(path)).thenReturn(Optional.of(meditation));
        MeditationDto meditationDto = meditationService.findMeditationByPath(path);
        assertEquals(title, meditationDto.getTitle());
        assertEquals(path, meditationDto.getPath());
        assertEquals(goal, meditationDto.getGoal());
    }

    @Test
    public void getMeditationByMathDoesNotExist() {
        when(meditationRepository.findByPath(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> meditationService.findMeditationByPath(null));
    }

    @Test
    public void getMeditationById() {
        long id = 1L;
        String title = "title";
        String path = "path";
        int goal = 100;

        Meditation meditation = new Meditation();
        meditation.setId(id);
        meditation.setTitle(title);
        meditation.setPath(path);
        meditation.setGoal(goal);

        when(meditationRepository.findById(meditation.getId())).thenReturn(Optional.of(meditation));
        MeditationDto meditationDto = meditationService.getById(meditation.getId());
        assertEquals(id, meditationDto.getId());
        assertEquals(title, meditationDto.getTitle());
        assertEquals(path, meditationDto.getPath());
        assertEquals(goal, meditationDto.getGoal());
    }

    @Test
    public void getMeditationByIdDoesNotExist() {
        when(meditationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> meditationService.getById(1L));
    }

    @Test
    public void getAll() {
        Meditation meditation1 = new Meditation();
        meditation1.setTitle("m1");

        Meditation meditation2 = new Meditation();
        meditation2.setTitle("m2");

        when(meditationRepository.findAll()).thenReturn(Arrays.asList(meditation1, meditation2));
        List<MeditationDto> meditations = meditationService.findAll();
        assertEquals(1, meditations.stream().filter(m -> m.getTitle().equals("m1")).count());
        assertEquals(1, meditations.stream().filter(m -> m.getTitle().equals("m2")).count());
        assertEquals(2, meditations.size());
    }

    @Test
    public void getAllEmpty() {
        when(meditationRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(meditationService.findAll().isEmpty());
    }
}
