package com.ngondrotracker.meditation.service;

import com.ngondrotracker.meditation.repository.PracticeResultRepository;
import com.ngondrotracker.meditation.service.impl.PracticeResultServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PracticeResultServiceImplTest {
    @Mock
    private PracticeResultRepository practiceResultRepository;

    @InjectMocks
    private PracticeResultServiceImpl practiceResultService;

    @Test
    public void addResult() {

    }

    @Test
    public void deleteExistingResult() {

    }

    @Test
    public void deleteNotExistingResult() {

    }

    @Test
    public void deleteResultOfAnotherUser() {

    }

    @Test
    public void getResultByUserAndMeditation() {

    }

    @Test
    public void getResultByUserAndMeditationWithNotExistingUser() {

    }

    @Test
    public void getResultByUserAndMeditationWithNotExistingMeditation() {

    }

    @Test
    public void getResultByUserAndMeditationWithEmptyResult() {

    }

    @Test
    public void getSummaryByUserAndMeditation() {

    }

    @Test
    public void getSummaryByUserAndMeditationWithNotExistingUser() {

    }

    @Test
    public void getSummaryByUserAndMeditationWithNotExistingMeditation() {

    }

    @Test
    public void getSummaryByUserAndMeditationWithEmptyResult() {

    }
}
