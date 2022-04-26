package com.ngondrotracker.meditation.repository;

import com.ngondrotracker.meditation.entity.PracticeResult;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PracticeResultRepository extends CrudRepository<PracticeResult, Long> {
    List<PracticeResult> findAllByUserAndMeditation(Long userId, Long meditationId);
}
