package com.ngondrotracker.meditation.repository;

import com.ngondrotracker.meditation.entity.PracticeResult;
import org.springframework.data.repository.CrudRepository;

public interface PracticeResultRepository extends CrudRepository<PracticeResult, Long> {
    Iterable<PracticeResult> findAllByUserAndMeditation(Long userId, Long meditationId);
}
