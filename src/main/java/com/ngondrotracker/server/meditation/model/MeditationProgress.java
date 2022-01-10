package com.ngondrotracker.server.meditation.model;

import com.ngondrotracker.server.user.model.User;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class MeditationProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Meditation meditation;

    private User user;

    private Integer totalCount;
}
