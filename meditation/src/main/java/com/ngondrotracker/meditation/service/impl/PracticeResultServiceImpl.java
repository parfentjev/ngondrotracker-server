package com.ngondrotracker.meditation.service.impl;

import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.dto.PracticeResultDto;
import com.ngondrotracker.meditation.dto.PracticeSummaryDto;
import com.ngondrotracker.meditation.entity.PracticeResult;
import com.ngondrotracker.meditation.repository.PracticeResultRepository;
import com.ngondrotracker.meditation.service.MeditationService;
import com.ngondrotracker.meditation.service.PracticeResultService;
import com.ngondrotracker.user.dto.UserDto;
import com.ngondrotracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ngondrotracker.meditation.util.MeditationUtils.practiceResultMapper;

@Service
public class PracticeResultServiceImpl implements PracticeResultService {
    @Autowired
    private PracticeResultRepository resultRepository;

    @Autowired
    private MeditationService meditationService;

    @Autowired
    private UserService userService;

    @Override
    public PracticeResultDto addResult(PracticeResultDto practiceResultDto) {
        verifyUserAndMeditationExist(practiceResultDto.getUserDto(), practiceResultDto.getMeditationDto());
        PracticeResult practiceResult = resultRepository.save(practiceResultMapper().dtoToEntity(practiceResultDto));

        return practiceResultMapper().entityToDto(practiceResult);
    }

    @Override
    public void deleteResult(PracticeResultDto practiceResultDto) {
        verifyUserAndMeditationExist(practiceResultDto.getUserDto(), practiceResultDto.getMeditationDto());

        UserDto userDto = userService.findUserByEmail(practiceResultDto.getUserDto().getEmail());
        PracticeResult practiceResult = resultRepository.findById(practiceResultDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Practice result", "id", String.valueOf(practiceResultDto.getId())));

        if (!userDto.getEmail().equals(practiceResult.getUser().getEmail())) {
            throw new AccessDeniedException("Can't access practice result of another user.");
        }

        resultRepository.delete(practiceResult);
    }

    @Override
    public List<PracticeResultDto> getResult(UserDto userDto, MeditationDto meditationDto) {
        return null; // todo
    }

    @Override
    public PracticeSummaryDto getSummary(UserDto userDto, MeditationDto meditationDto) {
        return null; // todo
    }

    private void verifyUserAndMeditationExist(UserDto userDto, MeditationDto meditationDto) {
        UserDto userByEmail = userService.findUserByEmail(userDto.getEmail());
        MeditationDto meditationByPath = meditationService.getByPath(meditationDto.getPath());

        if (userByEmail == null) {
            throw new ResourceNotFoundException("User", "email", userDto.getEmail());
        }

        if (meditationByPath == null) {
            throw new ResourceNotFoundException("Meditation", "path", String.valueOf(meditationDto.getPath()));
        }
    }
}
