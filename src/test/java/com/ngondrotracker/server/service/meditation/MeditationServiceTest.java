package com.ngondrotracker.server.service.meditation;

import com.ngondrotracker.server.common.exception.ItemAlreadyExistsException;
import com.ngondrotracker.server.common.exception.ItemDoesNotExist;
import com.ngondrotracker.server.meditation.model.Meditation;
import com.ngondrotracker.server.meditation.model.MeditationDto;
import com.ngondrotracker.server.meditation.model.MeditationRepository;
import com.ngondrotracker.server.meditation.service.MeditationServiceImpl;
import com.ngondrotracker.server.service.AbstractServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeditationServiceTest extends AbstractServiceTest {
    @Mock
    private MeditationRepository meditationRepository;

    @InjectMocks
    private MeditationServiceImpl meditationService;

    @Test
    @DisplayName("create meditation")
    public void createMeditation() throws Exception {
        when(meditationRepository.save(any())).thenReturn(new Meditation());
        meditationService.create("title", "path", 111111);
        verify(meditationRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("create meditation - already exists")
    public void createMeditationThatAlreadyExists() {
        when(meditationRepository.findByPath(any())).thenReturn(Optional.of(new Meditation()));
        assertThrows(ItemAlreadyExistsException.class, () -> meditationService.create("title", "path", 1));
    }

    @Test
    @DisplayName("get by path")
    public void getByPath() {
        Meditation meditation = new Meditation();
        meditation.setTitle("title");
        meditation.setPath("path");
        meditation.setGoal(111111);

        when(meditationRepository.findByPath(any())).thenReturn(Optional.of(meditation));
        MeditationDto meditationDto = meditationService.getByPath("test");

        assertEquals("title", meditationDto.getTitle());
        assertEquals("path", meditationDto.getPath());
        assertEquals(111111, meditationDto.getGoal());
    }

    @Test
    @DisplayName("get by path - path does not exist")
    public void getByPathThatDoesNotExist() {
        when(meditationRepository.findByPath(any())).thenReturn(Optional.empty());
        assertThrows(ItemDoesNotExist.class, () -> meditationService.getByPath("test"));
    }

    @Test
    @DisplayName("get all meditations")
    public void getAll() {
        Meditation meditation1 = new Meditation();
        meditation1.setTitle("title1");
        meditation1.setPath("path1");
        meditation1.setGoal(111111);

        Meditation meditation2 = new Meditation();
        meditation2.setTitle("title2");
        meditation2.setPath("path2");
        meditation2.setGoal(222222);

        when(meditationRepository.findAll()).thenReturn(Arrays.asList(meditation1, meditation2));
        List<MeditationDto> meditations = meditationService.getAll();

        assertEquals("title1", meditations.get(0).getTitle());
        assertEquals("path1", meditations.get(0).getPath());
        assertEquals(111111, meditations.get(0).getGoal());

        assertEquals("title2", meditations.get(1).getTitle());
        assertEquals("path2", meditations.get(1).getPath());
        assertEquals(222222, meditations.get(1).getGoal());
    }

    @Test
    @DisplayName("get all meditations - empty list")
    public void getAllButListIsEmpty() {
        when(meditationRepository.findAll()).thenReturn(new ArrayList<>());
        List<MeditationDto> meditations = meditationService.getAll();

        assertEquals(0, meditations.size());
    }
}
