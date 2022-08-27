package com.finalproject.dontbeweak.repository.pill;

import com.finalproject.dontbeweak.model.pill.Pill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PillRepository extends JpaRepository<Pill, Long> {
    List<Pill> findByUser_Username(String username);
    boolean existsByUser_IdAndProductName(Long userId, String productName);
    Pill findByUser_IdAndProductName(Long userId, String productName);
}
