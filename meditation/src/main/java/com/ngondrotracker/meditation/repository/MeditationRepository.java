package com.ngondrotracker.meditation.repository;

import com.ngondrotracker.meditation.entity.Meditation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeditationRepository extends CrudRepository<Meditation, Long> {
    Optional<Meditation> findByPath(String path);

    List<Meditation> findAll();
}
