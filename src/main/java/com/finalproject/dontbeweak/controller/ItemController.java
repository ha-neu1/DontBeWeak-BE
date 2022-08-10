package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.ItemRequestDto;
import com.finalproject.dontbeweak.model.Item;
import com.finalproject.dontbeweak.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    //아이템 등록
    @PostMapping("/items")
    public ResponseEntity<Item> inputItem(@ModelAttribute ItemRequestDto itemRequestDto) {
        itemService.inputItem(itemRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
