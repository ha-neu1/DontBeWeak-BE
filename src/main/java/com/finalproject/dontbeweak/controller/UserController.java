package com.finalproject.dontbeweak.controller;


import com.finalproject.dontbeweak.dto.LoginIdCheckDto;
import com.finalproject.dontbeweak.dto.SignupRequestDto;
import com.finalproject.dontbeweak.dto.SocialLoginInfoDto;
import com.finalproject.dontbeweak.auth.UserDetailsImpl;
import com.finalproject.dontbeweak.service.KakaoService;
import com.finalproject.dontbeweak.service.NaverService;
import com.finalproject.dontbeweak.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final NaverService naverService;

    //회원가입 요청 처리
    @PostMapping("/user/signup")
    @ApiOperation(value = "회원가입 요청 처리")
    public String registerUser(@Valid @RequestBody SignupRequestDto requestDto){
        String res = userService.registerUser(requestDto);
        if(res.equals("")){
            return "회원가입 성공";
        }else{
            return res;
        }
    }

    //카카오 소셜 로그인
    @GetMapping("/auth/kakao/callback")
    @ApiOperation(value = "카카오 소셜 로그인")
    public @ResponseBody SocialLoginInfoDto kakaoCallback(String code, HttpServletResponse response) {      //ResponseBody -> Data를 리턴해주는 컨트롤러 함수
        return kakaoService.requestKakao(code, response);
    }

    //네이버 소셜 로그인
    @GetMapping("/auth/naver/callback")
    @ApiOperation(value = "네이버 소셜 로그인")
    public @ResponseBody SocialLoginInfoDto naverCallback(String code, String state, HttpServletResponse response){
        return naverService.requestNaver(code, state, response);
    }

    //로그인 유저 정보
    @GetMapping("/user")
    @ApiOperation(value = "로그인 유저 정보", notes = "로그인 한 사용자 정보를 조회한다.")
    public LoginIdCheckDto userDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.userInfo(userDetails);
    }
}