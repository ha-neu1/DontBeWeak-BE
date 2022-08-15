package com.finalproject.dontbeweak.repository;

import com.finalproject.dontbeweak.model.Pill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PillRepository extends JpaRepository<Pill, Long> {
    Pill findByUser_Id(Long userId);
}
