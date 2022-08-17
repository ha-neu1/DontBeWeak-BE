package com.finalproject.dontbeweak.controller;


import com.finalproject.dontbeweak.dto.SignupRequestDto;
import com.finalproject.dontbeweak.model.CatImage;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import com.finalproject.dontbeweak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(@Valid @RequestBody SignupRequestDto requestDto, CatImage catImage){
        String res = userService.registerUser(requestDto, catImage);
        if(res.equals("")){
            return "회원가입 성공";
        }else{
            return res;
        }
    }

    //로그인 유저 정보
    @GetMapping("/user/login/auth")
    public User userDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.userInfo(userDetails);
    }
}