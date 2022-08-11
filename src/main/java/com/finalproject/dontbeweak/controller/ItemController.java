package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.ItemRequestDto;
import com.finalproject.dontbeweak.dto.ItemResponseDto;
import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.exception.ErrorCode;
import com.finalproject.dontbeweak.model.Item;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import com.finalproject.dontbeweak.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    //아이템 등록
    @PostMapping("/items")
    public ResponseEntity<Item> inputItem(@ModelAttribute ItemRequestDto itemRequestDto) throws IOException {
        itemService.inputItem(itemRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    //아이템 목록 조회
    @GetMapping("/items")
    public ResponseEntity<List<ItemResponseDto>> getItem(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            // 유저가 없다는 의미이므로 비정상 페이지 리턴
            throw new CustomException(ErrorCode.LOGIN_CHECK_CODE);
        } else {
            List<ItemResponseDto> itemResponseDtoList = itemService.getItem(userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(itemResponseDtoList);
        }
    }

    //아이템 구입
    @PostMapping("/items/{itemId}")
    public ResponseEntity<String> buyItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null) {
            // 유저가 없다는 의미이므로 비정상 페이지 리턴
            throw new CustomException(ErrorCode.LOGIN_CHECK_CODE);
        } else {
            itemService.buyItem(itemId, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body("아이템을 구매하였습니다.");
        }
    }
}
