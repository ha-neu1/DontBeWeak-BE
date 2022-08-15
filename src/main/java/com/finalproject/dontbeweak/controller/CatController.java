package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.CatResponseDto;
import com.finalproject.dontbeweak.service.CatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
}
