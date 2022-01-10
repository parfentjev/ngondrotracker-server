package com.ngondrotracker.server.meditation.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MeditationRepository extends CrudRepository<Meditation, String> {
    Optional<Meditation> findByPath(String path);

    List<Meditation> findAll();
}
