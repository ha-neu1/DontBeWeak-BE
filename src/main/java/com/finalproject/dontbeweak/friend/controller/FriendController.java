package com.finalproject.dontbeweak.friend.controller;

import com.finalproject.dontbeweak.friend.dto.FriendRequestDto;
import com.finalproject.dontbeweak.friend.dto.FriendResponseDto;
import com.finalproject.dontbeweak.friend.model.Friend;
import com.finalproject.dontbeweak.login.auth.UserDetailsImpl;
import com.finalproject.dontbeweak.friend.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class FriendController {
    private FriendService friendService;


    //친구 추가
    @PostMapping("/friend")
    public ResponseEntity<Friend> addfriend(@RequestBody FriendRequestDto friendRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        friendService.addfriend(friendRequestDto,userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(null);
    }

    //친구 목록 조회
    @GetMapping("/friend")
    public  ResponseEntity<List<FriendResponseDto>> listfriend(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<FriendResponseDto> friendResponseDtoList = friendService.listfriend(userDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(friendResponseDtoList);
    }
}
