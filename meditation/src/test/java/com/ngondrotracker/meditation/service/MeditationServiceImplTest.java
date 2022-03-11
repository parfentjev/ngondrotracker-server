package com.ngondrotracker.meditation.service;

import com.ngondrotracker.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.common.exception.ItemDoesNotExist;
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
    public void createMeditation() throws ItemAlreadyExistsException {
        String title = "newMeditation";
        String path = "meditationPath";
        int goal = 100;

        MeditationDto meditationDto = meditationService.create(title, path, goal);
        verify(meditationRepository, times(1)).save(any());
        assertEquals(title, meditationDto.getTitle());
        assertEquals(path, meditationDto.getPath());
        assertEquals(goal, meditationDto.getGoal());
    }

    @Test
    public void createMeditationThatAlreadyExists() {
        when(meditationRepository.findByPath(any())).thenReturn(Optional.of(new Meditation()));
        assertThrows(ItemAlreadyExistsException.class, () -> meditationService.create(null, null, 0));
        verify(meditationRepository, times(0)).save((any()));
    }

    @Test
    public void getMeditationByMath() {
        String title = "title";
        String path = "path";
        int goal = 100;

        Meditation meditation = new Meditation();
        meditation.setTitle(title);
        meditation.setPath(path);
        meditation.setGoal(goal);

        when(meditationRepository.findByPath(path)).thenReturn(Optional.of(meditation));
        MeditationDto meditationDto = meditationService.getByPath(path);
        assertEquals(title, meditationDto.getTitle());
        assertEquals(path, meditationDto.getPath());
        assertEquals(goal, meditationDto.getGoal());
    }

    @Test
    public void getMeditationByMathDoesNotExist() {
        when(meditationRepository.findByPath(any())).thenReturn(Optional.empty());
        assertThrows(ItemDoesNotExist.class, () -> meditationService.getByPath(null));
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