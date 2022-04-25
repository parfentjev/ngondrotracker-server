package com.ngondrotracker.meditation.util;

import com.ngondrotracker.meditation.util.mapper.MeditationMapper;
import com.ngondrotracker.meditation.util.mapper.PracticeResultMapper;

public class MeditationUtils {
    public static MeditationMapper meditationMapper() {
        return new MeditationMapper();
    }

    public static PracticeResultMapper practiceResultMapper() {
        return new PracticeResultMapper();
    }
}
