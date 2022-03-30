package com.ngondrotracker.meditation.entity;

import com.ngondrotracker.user.dto.UserDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.TimeZone;

@Data
public class PracticeResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "time_zone", nullable = false)
    private TimeZone timeZone;

    @Column(name = "repetitions", nullable = false)
    private Integer repetitions;

    @Column(name = "user_id", nullable = false)
    private UserDto user;

    @Column(name = "meditation_id", nullable = false)
    private Meditation meditation;
}
