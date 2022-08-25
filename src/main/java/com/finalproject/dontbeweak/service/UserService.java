package com.finalproject.dontbeweak.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.finalproject.dontbeweak.dto.LoginIdCheckDto;
import com.finalproject.dontbeweak.dto.SignupRequestDto;
import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.exception.ErrorCode;
import com.finalproject.dontbeweak.model.Cat;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.CatRepository;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CatRepository catRepository;
    private final CatService catService;

    //일반 회원가입
    public String registerUser(SignupRequestDto requestDto){
        String error = "";
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String passwordCheck = requestDto.getPasswordCheck();
        String nickname = requestDto.getNickname();
//        String pattern = "^[a-zA-Z0-9]*$";
        String pattern = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";

        //회원 username 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if(found.isPresent()){
            throw new CustomException(ErrorCode.USERNAME_DUPLICATION_CODE);
        }

        //회원가입 조건
        if(!Pattern.matches(pattern, username)){
            throw new CustomException(ErrorCode.USERNAME_FORM_CODE);
        }
        if (!password.equals(passwordCheck)){
            throw new CustomException(ErrorCode.PASSWORD_CHECK_CODE);
        } else if (password.length() < 4) {
            throw new CustomException(ErrorCode.PASSWORD_LENGTH_CODE);
        }

        //패스워드 인코딩
        password = passwordEncoder.encode(password);
        requestDto.setPassword(password);

        //유저 정보 저장
        User user = new User(username, password, nickname);
        user.setRole("ROLE_USER");
        userRepository.save(user);

        // 회원가입 후 사용자의 새 고양이 자동 생성
        catService.createNewCat(user);

        return error;
    }

    //로그인 유저 정보 반환
    public LoginIdCheckDto userInfo(UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        String nickname = userDetails.getUser().getNickname();
        int point = userDetails.getUser().getPoint();

        Optional<Cat> catTemp = catRepository.findByUser_Username(username);
        int level = catTemp.get().getLevel();
        int exp = catTemp.get().getExp();

        LoginIdCheckDto userInfo = new LoginIdCheckDto(username, nickname, point, level, exp);
        return userInfo;
    }

    //소셜로그인 토큰 발급
    public String JwtTokenCreate(String username){
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
                .withClaim("username", username)
                .sign(Algorithm.HMAC512("thwjd2"));
        return jwtToken;
    }
}
