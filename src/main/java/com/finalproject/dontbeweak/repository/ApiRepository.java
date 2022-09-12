package com.finalproject.dontbeweak.repository;


import com.finalproject.dontbeweak.model.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<Api,Long> {
    Page<Api> findAllByPRDUCTLessThan(String product, Pageable pageNo);
}