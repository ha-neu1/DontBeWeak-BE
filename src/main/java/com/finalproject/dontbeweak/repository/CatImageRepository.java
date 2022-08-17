package com.finalproject.dontbeweak.repository;

import com.finalproject.dontbeweak.model.CatImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatImageRepository extends JpaRepository<CatImage, Long> {
    CatImage findCatImageByChangeLevel(int level);

}
