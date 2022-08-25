package com.finalproject.dontbeweak.pill.repository;

import com.finalproject.dontbeweak.pill.model.PillHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PillHistoryRepository extends JpaRepository<PillHistory, Long> {
    List<PillHistory> findAllByUser_IdAndUsedAtBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
