package com.finalproject.dontbeweak.cat.repository;

import com.finalproject.dontbeweak.cat.model.CatImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatImageRepository extends JpaRepository<CatImage, Long> {
    Optional<CatImage> findCatImageByChangeLevel(int level);
}
