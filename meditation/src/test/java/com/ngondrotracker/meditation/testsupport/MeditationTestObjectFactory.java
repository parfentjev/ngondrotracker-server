package com.ngondrotracker.meditation.testsupport;

import com.ngondrotracker.meditation.dto.MeditationDto;
import com.ngondrotracker.meditation.dto.PracticeResultDto;
import com.ngondrotracker.meditation.entity.Meditation;
import com.ngondrotracker.meditation.entity.PracticeResult;
import com.ngondrotracker.user.dto.UserDto;
import com.ngondrotracker.user.entity.User;

import java.util.TimeZone;

public class MeditationTestObjectFactory {
    public Meditation meditation(long id) {
        Meditation meditation = new Meditation();
        meditation.setId(id);

        return meditation;
    }

    public MeditationDto meditationDto(long id) {
        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setId(id);

        return meditationDto;
    }

    public MeditationDto meditationDto(long id, String path) {
        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setId(id);
        meditationDto.setPath(path);

        return meditationDto;
    }

    public MeditationDto meditationDto(long id, String title, String path, int goal, int order) {
        MeditationDto meditationDto = new MeditationDto();
        meditationDto.setId(id);
        meditationDto.setTitle(title);
        meditationDto.setPath(path);
        meditationDto.setGoal(goal);
        meditationDto.setOrder(order);

        return meditationDto;
    }

    public PracticeResult practiceResult(long id, long timestamp, String timeZone, int repetitions, User user, Meditation meditation) {
        PracticeResult practiceResult = new PracticeResult();
        practiceResult.setId(id);
        practiceResult.setTimestamp(timestamp);
        practiceResult.setTimeZone(TimeZone.getTimeZone(timeZone));
        practiceResult.setRepetitions(repetitions);
        practiceResult.setUser(user);
        practiceResult.setMeditation(meditation);

        return practiceResult;
    }

    public PracticeResultDto practiceResultDto(long id) {
        PracticeResultDto practiceResultDto = new PracticeResultDto();
        practiceResultDto.setId(id);

        return practiceResultDto;
    }

    public PracticeResultDto practiceResultDto(long id, long timestamp, String timeZone, int repetitions, UserDto userDto, MeditationDto meditationDto) {
        PracticeResultDto practiceResultDto = new PracticeResultDto();
        practiceResultDto.setId(id);
        practiceResultDto.setTimestamp(timestamp);
        practiceResultDto.setTimeZone(TimeZone.getTimeZone(timeZone));
        practiceResultDto.setRepetitions(repetitions);
        practiceResultDto.setUserDto(userDto);
        practiceResultDto.setMeditationDto(meditationDto);

        return practiceResultDto;
    }

    public User user(long id) {
        User user = new User();
        user.setId(id);

        return user;
    }

    public UserDto userDto(long id) {
        UserDto userDto = new UserDto();
        userDto.setId(id);

        return userDto;
    }

    public User user(long id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);

        return user;
    }

    public UserDto userDto(long id, String email) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setEmail(email);

        return userDto;
    }
}
