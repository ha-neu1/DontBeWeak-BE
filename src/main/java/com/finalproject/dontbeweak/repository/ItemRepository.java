package com.finalproject.dontbeweak.repository;

import com.finalproject.dontbeweak.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findItemById(Long id);
}
