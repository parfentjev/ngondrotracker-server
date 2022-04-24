package com.ngondrotracker.meditation.service;

import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.dto.PracticeResultDto;
import com.ngondrotracker.meditation.dto.PracticeSummaryDto;
import com.ngondrotracker.meditation.entity.Meditation;
import com.ngondrotracker.meditation.entity.PracticeResult;
import com.ngondrotracker.meditation.repository.PracticeResultRepository;
import com.ngondrotracker.meditation.service.impl.MeditationServiceImpl;
import com.ngondrotracker.meditation.service.impl.PracticeResultServiceImpl;
import com.ngondrotracker.user.dto.UserDto;
import com.ngondrotracker.user.entity.User;
import com.ngondrotracker.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ngondrotracker.meditation.testsupport.MeditationTestSupport.testObjectFactory;
import static org.assertj.core.util.Lists.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PracticeResultServiceImplTest {
    @Mock
    private PracticeResultRepository practiceResultRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private MeditationServiceImpl meditationService;

    @InjectMocks
    private PracticeResultServiceImpl practiceResultService;

    @Test
    public void addResult() {
        Meditation meditation = testObjectFactory().meditation(1L);
        MeditationDto meditationDto = testObjectFactory().meditationDto(1L);

        User user = testObjectFactory().user(2L);
        UserDto userDto = testObjectFactory().userDto(2L);

        PracticeResult practiceResult = testObjectFactory().practiceResult(3L, 33L, "Europe/Tallinn", 108, user, meditation);
        PracticeResultDto practiceResultDto = testObjectFactory().practiceResultDto(3L, 33L, "Europe/Tallinn", 108, userDto, meditationDto);

        when(userService.findUserByEmail("username")).thenReturn(userDto);
        when(practiceResultRepository.save(any())).thenReturn(practiceResult);

        PracticeResultDto responseDto = practiceResultService.addResult(practiceResultDto);
        assertEquals(practiceResultDto.getId(), responseDto.getId());
        assertEquals(practiceResultDto.getTimestamp(), responseDto.getTimestamp());
        assertEquals(practiceResultDto.getTimeZone(), responseDto.getTimeZone());
        assertEquals(practiceResultDto.getRepetitions(), responseDto.getRepetitions());
        assertEquals(practiceResultDto.getUserDto().getId(), responseDto.getUserDto().getId());
        assertEquals(practiceResultDto.getMeditationDto().getId(), responseDto.getMeditationDto().getId());

        verify(practiceResultRepository, times(1)).save(practiceResult);
    }

    @Test
    public void deleteExistingResult() {
        PracticeResultDto practiceResultDto = testObjectFactory().practiceResultDto(1L);
        User user = testObjectFactory().user(1L);
        UserDto userDto = testObjectFactory().userDto(1L);

        PracticeResult practiceResult = testObjectFactory().practiceResult(1L, 1L, null, 1, user, null);

        when(userService.findUserByEmail("username")).thenReturn(userDto);
        when(practiceResultRepository.findById(1L)).thenReturn(Optional.of(practiceResult));

        practiceResultService.deleteResult(practiceResultDto);
        verify(practiceResultRepository, times(1)).delete(practiceResult);
    }

    @Test
    public void deleteNotExistingResult() {
        PracticeResultDto practiceResultDto = testObjectFactory().practiceResultDto(1L);

        PracticeResult practiceResult = new PracticeResult();
        when(practiceResultRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> practiceResultService.deleteResult(practiceResultDto));
        verify(practiceResultRepository, times(0)).delete(practiceResult);
    }

    @Test
    public void deleteResultOfAnotherUser() {
        PracticeResultDto practiceResultDto = testObjectFactory().practiceResultDto(1L);

        UserDto resultRequester = testObjectFactory().userDto(1L);
        User resultOwner = testObjectFactory().user(2L);
        PracticeResult practiceResult = testObjectFactory().practiceResult(1L, 1L, null, 1, resultOwner, null);

        when(userService.findUserByEmail("username")).thenReturn(resultRequester);
        when(practiceResultRepository.findById(1L)).thenReturn(Optional.of(practiceResult));

        practiceResultService.deleteResult(practiceResultDto);
        verify(practiceResultRepository, times(0)).delete(practiceResult);
    }

    @Test
    public void getResultByUserAndMeditation() {
        UserDto userDto = testObjectFactory().userDto(1L);
        MeditationDto meditationDto = testObjectFactory().meditationDto(1L);

        User user = testObjectFactory().user(1L);
        Meditation meditation = testObjectFactory().meditation(1L);

        PracticeResult practiceResult1 = testObjectFactory().practiceResult(1L, 1650800014, "Europe/Tallinn", 108, user, meditation);
        PracticeResult practiceResult2 = testObjectFactory().practiceResult(2L, 1650800015, "Europe/Riga", 216, user, meditation);
        List<PracticeResult> practiceResultList = Arrays.asList(practiceResult1, practiceResult2);

        when(practiceResultRepository.findAllByUserAndMeditation(user.getId(), meditation.getId())).thenReturn(practiceResultList);

        List<PracticeResultDto> practiceResultDtoList = practiceResultService.getResult(userDto, meditationDto);
        assertEquals(2, practiceResultDtoList.size());

        PracticeResultDto practiceResultDto1 = practiceResultDtoList.get(0);
        assertEquals(practiceResult1.getId(), practiceResultDto1.getId());
        assertEquals(practiceResult1.getTimestamp(), practiceResultDto1.getTimestamp());
        assertEquals(practiceResult1.getRepetitions(), practiceResultDto1.getRepetitions());
        assertEquals(practiceResult1.getUser().getId(), practiceResultDto1.getUserDto().getId());
        assertEquals(practiceResult1.getMeditation().getId(), practiceResultDto1.getMeditationDto().getId());

        PracticeResultDto practiceResultDto2 = practiceResultDtoList.get(1);
        assertEquals(practiceResult2.getId(), practiceResultDto2.getId());
        assertEquals(practiceResult2.getTimestamp(), practiceResultDto2.getTimestamp());
        assertEquals(practiceResult2.getRepetitions(), practiceResultDto2.getRepetitions());
        assertEquals(practiceResult2.getUser().getId(), practiceResultDto2.getUserDto().getId());
        assertEquals(practiceResult2.getMeditation().getId(), practiceResultDto2.getMeditationDto().getId());
    }

    @Test
    public void getResultByUserAndMeditationWithResourceNotFoundException() {
        UserDto userDto = testObjectFactory().userDto(1L);
        MeditationDto meditationDto = testObjectFactory().meditationDto(2L);

        when(practiceResultRepository.findAllByUserAndMeditation(userDto.getId(), meditationDto.getId())).thenReturn(emptyList());
        assertThrows(ResourceNotFoundException.class, () -> practiceResultService.getResult(userDto, meditationDto));
    }

    @Test
    public void getSummaryByUserAndMeditation() {
        UserDto userDto = testObjectFactory().userDto(1L);
        MeditationDto meditationDto = testObjectFactory().meditationDto(1L);

        MeditationDto requestMeditationDto = new MeditationDto();
        requestMeditationDto.setPath("test");

        MeditationDto responseMeditationDto = testObjectFactory().meditationDto(1L, "title", "path", 1404, 0);

        User user = testObjectFactory().user(1L);
        Meditation meditation = testObjectFactory().meditation(1L);

        PracticeResult practiceResult1 = testObjectFactory().practiceResult(1L, 1650800014, "Europe/Tallinn", 108, user, meditation);
        PracticeResult practiceResult2 = testObjectFactory().practiceResult(2L, 1650800015, "Europe/Riga", 216, user, meditation);
        List<PracticeResult> practiceResultList = Arrays.asList(practiceResult1, practiceResult2);

        when(practiceResultRepository.findAllByUserAndMeditation(user.getId(), meditation.getId())).thenReturn(practiceResultList);
        when(meditationService.getByPath(requestMeditationDto.getPath())).thenReturn(meditationDto);

        PracticeSummaryDto practiceSummaryDto = practiceResultService.getSummary(userDto, meditationDto);
        assertEquals(responseMeditationDto.getId(), practiceSummaryDto.getMeditationDto().getId());
        assertEquals(requestMeditationDto.getTitle(), practiceSummaryDto.getMeditationDto().getTitle());
        assertEquals(requestMeditationDto.getGoal(), practiceSummaryDto.getMeditationDto().getGoal());

        assertEquals(216, practiceSummaryDto.getRepetitions());

        assertEquals(practiceResult2.getId(), practiceSummaryDto.getLatestPracticeResult().getId());
        assertEquals(practiceResult2.getMeditation().getId(), practiceSummaryDto.getLatestPracticeResult().getMeditationDto().getId());
        assertEquals(practiceResult2.getRepetitions(), practiceSummaryDto.getLatestPracticeResult().getRepetitions());
        assertEquals(practiceResult2.getTimestamp(), practiceSummaryDto.getLatestPracticeResult().getTimestamp());
        assertEquals(practiceResult2.getTimeZone(), practiceSummaryDto.getLatestPracticeResult().getTimeZone());

        assertEquals(LocalDate.now().plusDays(5L), practiceSummaryDto.getEstimatedCompletionDate());
    }

    @Test
    public void getSummaryByUserAndMeditationWithResourceNotFoundException() {
        UserDto userDto = testObjectFactory().userDto(1L);
        MeditationDto meditationDto = testObjectFactory().meditationDto(2L);

        when(practiceResultRepository.findAllByUserAndMeditation(userDto.getId(), meditationDto.getId())).thenReturn(emptyList());
        assertThrows(ResourceNotFoundException.class, () -> practiceResultService.getSummary(userDto, meditationDto));
    }
}
