package com.finalproject.dontbeweak.repository;

import com.finalproject.dontbeweak.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatRepository extends JpaRepository<Cat, Long> {
    Optional<Cat> findByUser_Username(String username);
}
