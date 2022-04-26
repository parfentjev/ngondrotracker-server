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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.ngondrotracker.meditation.util.MeditationUtils.practiceResultMapper;

@Service
public class PracticeResultServiceImpl implements PracticeResultService {
    @Autowired
    private PracticeResultRepository practiceResultRepository;

    @Autowired
    private MeditationService meditationService;

    @Autowired
    private UserService userService;

    @Override
    public PracticeResultDto addResult(PracticeResultDto practiceResultDto) {
        verifyUserAndMeditationExist(practiceResultDto.getUserDto(), practiceResultDto.getMeditationDto());
        PracticeResult practiceResult = practiceResultRepository.save(practiceResultMapper().dtoToEntity(practiceResultDto));

        return practiceResultMapper().entityToDto(practiceResult);
    }

    @Override
    public void deleteResult(PracticeResultDto practiceResultDto) {
        verifyUserAndMeditationExist(practiceResultDto.getUserDto(), practiceResultDto.getMeditationDto());

        UserDto userDto = userService.findUserByEmail(practiceResultDto.getUserDto().getEmail());
        PracticeResult practiceResult = practiceResultRepository.findById(practiceResultDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Practice result", "id", String.valueOf(practiceResultDto.getId())));

        if (!userDto.getEmail().equals(practiceResult.getUser().getEmail())) {
            throw new AccessDeniedException("Can't access practice result of another user.");
        }

        practiceResultRepository.delete(practiceResult);
    }

    @Override
    public List<PracticeResultDto> getResults(UserDto userDto, MeditationDto meditationDto) {
        verifyUserAndMeditationExist(userDto, meditationDto);

        List<PracticeResult> results = practiceResultRepository.findAllByUserAndMeditation(userDto.getId(), meditationDto.getId());
        if (results.isEmpty()) {
            throw new ResourceNotFoundException("Result", "user and meditation", userDto.getEmail() + " " + meditationDto.getPath());
        }

        return results
                .stream()
                .map(practiceResultMapper()::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PracticeSummaryDto getSummary(UserDto userDto, MeditationDto meditationDto) {
        verifyUserAndMeditationExist(userDto, meditationDto);

        List<PracticeResultDto> results = getResults(userDto, meditationDto);
        MeditationDto meditationByPath = meditationService.findMeditationByPath(meditationDto.getPath());
        int repetitions = results.stream().mapToInt(PracticeResultDto::getRepetitions).sum();
        PracticeResultDto latestPracticeResult = results.get(results.size() - 1);
        LocalDate estimatedCompletionDate = findEstimatedCompletionDate(latestPracticeResult.getRepetitions(), repetitions, meditationByPath.getGoal());

        PracticeSummaryDto practiceSummaryDto = new PracticeSummaryDto();
        practiceSummaryDto.setMeditationDto(meditationByPath);
        practiceSummaryDto.setRepetitions(repetitions);
        practiceSummaryDto.setLatestPracticeResult(latestPracticeResult);
        practiceSummaryDto.setEstimatedCompletionDate(estimatedCompletionDate);

        return practiceSummaryDto;
    }

    private void verifyUserAndMeditationExist(UserDto userDto, MeditationDto meditationDto) {
        UserDto userByEmail = userService.findUserByEmail(userDto.getEmail());
        MeditationDto meditationByPath = meditationService.findMeditationByPath(meditationDto.getPath());

        if (userByEmail == null) {
            throw new ResourceNotFoundException("User", "email", userDto.getEmail());
        }

        if (meditationByPath == null) {
            throw new ResourceNotFoundException("Meditation", "path", String.valueOf(meditationDto.getPath()));
        }
    }

    private LocalDate findEstimatedCompletionDate(int repetitionsPerDay, int currentAmount, int goal) {
        return LocalDate.now().plusDays((goal - currentAmount) / repetitionsPerDay);
    }
}
