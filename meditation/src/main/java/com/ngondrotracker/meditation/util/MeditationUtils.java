package com.ngondrotracker.meditation.util;

import com.ngondrotracker.meditation.util.mapper.MeditationMapper;

public class MeditationUtils {
    public static MeditationMapper meditationMapper() {
        return new MeditationMapper();
    }
}
