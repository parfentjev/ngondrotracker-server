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
import org.springframework.security.access.AccessDeniedException;

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
        MeditationDto meditationDto = testObjectFactory().meditationDto(1L, "path");

        User user = testObjectFactory().user(2L);
        UserDto userDto = testObjectFactory().userDto(2L);
        userDto.setEmail("username");

        PracticeResult practiceResult = testObjectFactory().practiceResult(3L, 33L, "Europe/Tallinn", 108, user, meditation);
        PracticeResultDto practiceResultDto = testObjectFactory().practiceResultDto(3L, 33L, "Europe/Tallinn", 108, userDto, meditationDto);

        when(userService.findUserByEmail(userDto.getEmail())).thenReturn(userDto);
        when(meditationService.findMeditationByPath(meditationDto.getPath())).thenReturn(meditationDto);
        when(practiceResultRepository.save(any())).thenReturn(practiceResult);

        PracticeResultDto responseDto = practiceResultService.addResult(practiceResultDto);
        assertEquals(practiceResultDto.getId(), responseDto.getId());
        assertEquals(practiceResultDto.getTimestamp(), responseDto.getTimestamp());
        assertEquals(practiceResultDto.getTimeZone(), responseDto.getTimeZone());
        assertEquals(practiceResultDto.getRepetitions(), responseDto.getRepetitions());
        assertEquals(practiceResultDto.getUserDto().getId(), responseDto.getUserDto().getId());
        assertEquals(practiceResultDto.getMeditationDto().getId(), responseDto.getMeditationDto().getId());

        verify(practiceResultRepository, times(1)).save(any());
    }

    @Test
    public void deleteExistingResult() {
        User user = testObjectFactory().user(1L, "username");
        UserDto userDto = testObjectFactory().userDto(1L, "username");

        Meditation meditation = testObjectFactory().meditation(1L);
        MeditationDto meditationDto = testObjectFactory().meditationDto(1L, "path");

        PracticeResultDto practiceResultDto = testObjectFactory().practiceResultDto(1L, 1L, "Europe/Tallinn", 1, userDto, meditationDto);
        PracticeResult practiceResult = testObjectFactory().practiceResult(1L, 1L, "Europe/Tallinn", 1, user, meditation);

        when(userService.findUserByEmail("username")).thenReturn(userDto);
        when(meditationService.findMeditationByPath(meditationDto.getPath())).thenReturn(meditationDto);
        when(practiceResultRepository.findById(1L)).thenReturn(Optional.of(practiceResult));

        practiceResultService.deleteResult(practiceResultDto);
        verify(practiceResultRepository, times(1)).delete(any());
    }

    @Test
    public void deleteNotExistingResult() {
        UserDto userDto = testObjectFactory().userDto(1L, "username");
        MeditationDto meditationDto = testObjectFactory().meditationDto(1L, "path");

        PracticeResultDto practiceResultDto = testObjectFactory().practiceResultDto(1L);
        practiceResultDto.setUserDto(userDto);
        practiceResultDto.setMeditationDto(meditationDto);

        when(userService.findUserByEmail(userDto.getEmail())).thenReturn(userDto);
        when(meditationService.findMeditationByPath(meditationDto.getPath())).thenReturn(meditationDto);
        when(practiceResultRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> practiceResultService.deleteResult(practiceResultDto));
        verify(practiceResultRepository, times(0)).delete(any());
    }

    @Test
    public void deleteResultOfAnotherUser() {
        UserDto resultRequester = testObjectFactory().userDto(1L, "username");
        MeditationDto meditationDto = testObjectFactory().meditationDto(1L, "path");

        PracticeResultDto practiceResultDto = testObjectFactory().practiceResultDto(1L, 1L, "Europe/Tallinn", 1, resultRequester, meditationDto);

        User resultOwner = testObjectFactory().user(2L, "username2");
        PracticeResult practiceResult = testObjectFactory().practiceResult(1L, 1L, "Europe/Tallinn", 1, resultOwner, new Meditation());

        when(userService.findUserByEmail("username")).thenReturn(resultRequester);
        when(meditationService.findMeditationByPath(meditationDto.getPath())).thenReturn(meditationDto);
        when(practiceResultRepository.findById(1L)).thenReturn(Optional.of(practiceResult));

        assertThrows(AccessDeniedException.class, () -> practiceResultService.deleteResult(practiceResultDto));
        verify(practiceResultRepository, times(0)).delete(any());
    }

    @Test
    public void getResultByUserAndMeditation() {
        UserDto userDto = testObjectFactory().userDto(1L, "username");
        MeditationDto meditationDto = testObjectFactory().meditationDto(2L, "path");

        User user = testObjectFactory().user(1L, "username");
        Meditation meditation = testObjectFactory().meditation(2L, "path");

        PracticeResult practiceResult1 = testObjectFactory().practiceResult(1L, 1650800014, "Europe/Tallinn", 108, user, meditation);
        PracticeResult practiceResult2 = testObjectFactory().practiceResult(2L, 1650800015, "Europe/Riga", 216, user, meditation);
        List<PracticeResult> practiceResultList = Arrays.asList(practiceResult1, practiceResult2);

        when(userService.findUserByEmail(userDto.getEmail())).thenReturn(userDto);
        when(meditationService.findMeditationByPath(meditationDto.getPath())).thenReturn(meditationDto);
        when(practiceResultRepository.findAllByUserAndMeditation(user.getId(), meditation.getId())).thenReturn(practiceResultList);

        List<PracticeResultDto> practiceResultDtoList = practiceResultService.getResults(userDto, meditationDto);
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
        UserDto userDto = testObjectFactory().userDto(1L, "username");
        MeditationDto meditationDto = testObjectFactory().meditationDto(2L, "path");

        when(userService.findUserByEmail(userDto.getEmail())).thenReturn(userDto);
        when(meditationService.findMeditationByPath(meditationDto.getPath())).thenReturn(meditationDto);
        when(practiceResultRepository.findAllByUserAndMeditation(userDto.getId(), meditationDto.getId())).thenReturn(emptyList());
        assertThrows(ResourceNotFoundException.class, () -> practiceResultService.getResults(userDto, meditationDto));
    }

    @Test
    public void getSummaryByUserAndMeditation() {
        UserDto userDto = testObjectFactory().userDto(1L, "username");
        MeditationDto meditationDto = testObjectFactory().meditationDto(1L, "title", "path", 1404, 0);
        User user = testObjectFactory().user(1L);
        Meditation meditation = testObjectFactory().meditation(1L);

        PracticeResult practiceResult1 = testObjectFactory().practiceResult(1L, 1650800014, "Europe/Tallinn", 108, user, meditation);
        PracticeResult practiceResult2 = testObjectFactory().practiceResult(2L, 1650800015, "Europe/Riga", 216, user, meditation);
        List<PracticeResult> practiceResultList = Arrays.asList(practiceResult1, practiceResult2);

        when(userService.findUserByEmail(userDto.getEmail())).thenReturn(userDto);
        when(meditationService.findMeditationByPath(meditationDto.getPath())).thenReturn(meditationDto);
        when(practiceResultRepository.findAllByUserAndMeditation(user.getId(), meditation.getId())).thenReturn(practiceResultList);

        PracticeSummaryDto practiceSummaryDto = practiceResultService.getSummary(userDto, meditationDto);
        assertEquals(meditationDto.getId(), practiceSummaryDto.getMeditationDto().getId());
        assertEquals(meditationDto.getTitle(), practiceSummaryDto.getMeditationDto().getTitle());
        assertEquals(meditationDto.getGoal(), practiceSummaryDto.getMeditationDto().getGoal());

        assertEquals(324, practiceSummaryDto.getRepetitions());

        assertEquals(practiceResult2.getId(), practiceSummaryDto.getLatestPracticeResult().getId());
        assertEquals(practiceResult2.getMeditation().getId(), practiceSummaryDto.getLatestPracticeResult().getMeditationDto().getId());
        assertEquals(practiceResult2.getRepetitions(), practiceSummaryDto.getLatestPracticeResult().getRepetitions());
        assertEquals(practiceResult2.getTimestamp(), practiceSummaryDto.getLatestPracticeResult().getTimestamp());
        assertEquals(practiceResult2.getTimeZone(), practiceSummaryDto.getLatestPracticeResult().getTimeZone());

        assertEquals(LocalDate.now().plusDays(5L), practiceSummaryDto.getEstimatedCompletionDate());
    }

    @Test
    public void getSummaryByUserAndMeditationWithResourceNotFoundException() {
        UserDto userDto = testObjectFactory().userDto(1L, "username");
        MeditationDto meditationDto = testObjectFactory().meditationDto(2L, "path");

        when(userService.findUserByEmail(userDto.getEmail())).thenReturn(userDto);
        when(meditationService.findMeditationByPath(meditationDto.getPath())).thenReturn(meditationDto);
        when(practiceResultRepository.findAllByUserAndMeditation(userDto.getId(), meditationDto.getId())).thenReturn(emptyList());
        assertThrows(ResourceNotFoundException.class, () -> practiceResultService.getSummary(userDto, meditationDto));
    }
}
