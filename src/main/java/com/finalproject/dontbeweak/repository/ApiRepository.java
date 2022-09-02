package com.finalproject.dontbeweak.repository;


import com.finalproject.dontbeweak.model.Api;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<Api,Long> {
}