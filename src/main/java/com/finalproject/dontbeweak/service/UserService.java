package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.jwtwithredis.*;
import com.finalproject.dontbeweak.dto.LoginIdCheckDto;
import com.finalproject.dontbeweak.dto.SignupRequestDto;
import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.exception.ErrorCode;
import com.finalproject.dontbeweak.model.Cat;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.CatRepository;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CatRepository catRepository;
    private final CatService catService;
    private final RedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final Response response;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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

//    //소셜로그인 토큰 발급
//    public String JwtTokenCreate(String username){
//        String jwtToken = JWT.create()
//                .withSubject("cos토큰")
//                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
//                .withClaim("username", username)
//                .sign(Algorithm.HMAC512("thwjd2"));
//        return jwtToken;
//    }


    // 로그인 시 refresh token, access token 생성 및 저장
    public ResponseEntity<?> login(UserRequestDto.Login login) {

        if (userRepository.findByUsername(login.getUsername()).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    // 액세스 토큰 재발급
        public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue) {
            // 1. Refresh Token 검증
            if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
                return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
            }

            // 2. Access Token 에서 Username 을 가져옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

            // 3. Redis 에서 Username을 기반으로 저장된 Refresh Token 값을 가져옵니다.
            String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
            // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
            if(ObjectUtils.isEmpty(refreshToken)) {
                return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
            }
            if(!refreshToken.equals(reissue.getRefreshToken())) {
                return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
            }

            // 4. 새로운 토큰 생성
            UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            // 5. RefreshToken Redis 업데이트
            redisTemplate.opsForValue()
                    .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
        }



    // 로그아웃
    public ResponseEntity<?> logout(UserRequestDto.Logout logout) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 Username을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 Username으로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
            System.out.println("=== 리프레시 토큰 삭제 완료 ===");
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

//    public ResponseEntity<?> authority() {
//        // SecurityContext에 담겨 있는 authentication userEamil 정보
//        String user = ;
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));
//
//        // add ROLE_ADMIN
//        user.getRole().add(Authority.ROLE_ADMIN.name());
//        userRepository.save(user);
//
//        return response.success();
//    }
}
