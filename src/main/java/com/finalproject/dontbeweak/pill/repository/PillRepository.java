package com.finalproject.dontbeweak.pill.repository;

import com.finalproject.dontbeweak.pill.model.Pill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PillRepository extends JpaRepository<Pill, Long> {
    List<Pill> findByUser_Username(String username);
    Optional<Pill> findPillByProductName(String productName);
    Pill findByUser_IdAndProductName(Long userId, String productName);
}
