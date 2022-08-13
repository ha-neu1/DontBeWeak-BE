package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.BuyItemResponseDto;
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
    //남들이 등록할 수 없게 권한처리 해야함..(어떻게하지,,?)
//    @PostMapping("/items")
//    public ResponseEntity<Item> inputItem(@RequestBody ItemRequestDto itemRequestDto) throws IOException {
//        itemService.inputItem(itemRequestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(null);
//    }
//


    //아이템 목록 조회
    @GetMapping("/items")
    public ResponseEntity<List<ItemResponseDto>> getItem(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            // 유저가 없다는 의미이므로 비정상 페이지 리턴
            throw new CustomException(ErrorCode.LOGIN_CHECK_CODE);
        } else {
            List<ItemResponseDto> itemResponseDtoList = itemService.getItem();
            return ResponseEntity.ok().body(itemResponseDtoList);
        }
    }

    //아이템 구입
//    @PostMapping("/items/{itemId}")
//    public ResponseEntity<String> buyItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        if (userDetails == null) {
//            // 유저가 없다는 의미이므로 비정상 페이지 리턴
//            throw new CustomException(ErrorCode.LOGIN_CHECK_CODE);
//        } else {
//            itemService.buyItem(itemId, userDetails);
//            return ResponseEntity.status(HttpStatus.CREATED).body("아이템을 구매하였습니다.");
//        }
//    }

   // 아이템 구입 및 적용
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<BuyItemResponseDto> patchItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null) {
            // 유저가 없다는 의미이므로 비정상 페이지 리턴
            throw new CustomException(ErrorCode.LOGIN_CHECK_CODE);
        } else {
            BuyItemResponseDto buyItemResponseDto = itemService.patchItem(itemId, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(buyItemResponseDto);
        }
    }


//    //아이템적용2
//    //구매한 사람 토큰 값과 적용누른 사람 토큰 값이 같아야 적용이 되게 해야함
//    @GetMapping("/items/{itemId}")
//    public ResponseEntity<BuyItemResponseDto> patchItem(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        if (userDetails == null) {
//            // 유저가 없다는 의미이므로 비정상 페이지 리턴
//            throw new CustomException(ErrorCode.LOGIN_CHECK_CODE);
//        } else {
//            BuyItemResponseDto buyItemResponseDto = itemService.patchItem(userDetails.getUser());
//            return ResponseEntity.ok().body(buyItemResponseDto);
//        }

//    }

    //아이템 등록
    @PostMapping("/items")
    public ResponseEntity<Item> inputItem(@RequestBody ItemRequestDto itemRequestDto) throws IOException {
        itemService.inputItem(itemRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
