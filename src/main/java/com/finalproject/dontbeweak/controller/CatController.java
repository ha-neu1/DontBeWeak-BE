package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.CatRequestDto;
import com.finalproject.dontbeweak.dto.CatResponseDto;
import com.finalproject.dontbeweak.model.Cat;
import com.finalproject.dontbeweak.service.CatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class CatController {

    private final CatService catService;


    // 내 고양이 조회
    @GetMapping("/cat")
    public ResponseEntity<CatResponseDto> getMyCatStatus
    (@AuthenticationPrincipal UserDetails userDetails) {

        CatResponseDto catResponseDto = catService.getMyCatStatus(userDetails);

        return ResponseEntity.status(HttpStatus.OK)
                .body(catResponseDto);
    }


    // 친구 고양이 조회
    @GetMapping("/cat/{username}")
    public ResponseEntity<CatResponseDto> getFriendCatStatus
    (@PathVariable("username") String username) {

        CatResponseDto friendCatResponseDto = catService.getFriendCatStatus(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(friendCatResponseDto);
    }

/*    // 고양이 레벨 조정 (테스트 용)
    @PatchMapping("/cat/{username}")
    public ResponseEntity<void> setCatLevelTest
    (@PathVariable("username") String username, @RequestBody CatRequestDto requestDto) {
        catService.setCatLevelTest(username, requestDto);
    }*/
}
