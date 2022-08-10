package com.finalproject.dontbeweak.repository;

import com.finalproject.dontbeweak.model.Pill;
import com.finalproject.dontbeweak.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PillRepository extends JpaRepository<Pill, Long> {
    List<Pill> findAllByPill(User user);
}
