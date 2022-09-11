package com.finalproject.dontbeweak.service;


import com.finalproject.dontbeweak.dto.LoginIdCheckDto;
import com.finalproject.dontbeweak.dto.SignupRequestDto;
import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.exception.ErrorCode;
import com.finalproject.dontbeweak.model.Cat;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.CatRepository;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.auth.UserDetailsImpl;
import com.finalproject.dontbeweak.repository.jwtwithredis.JwtTokenProvider;
import com.finalproject.dontbeweak.repository.jwtwithredis.Response;
import com.finalproject.dontbeweak.repository.jwtwithredis.UserRequestDto;
import com.finalproject.dontbeweak.repository.jwtwithredis.UserResponseDto;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final HttpServletResponse httpServletResponse;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";


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
    public ResponseEntity<?> reissue(HttpServletRequest httpServletRequest) {

        // 1. Request Header에서 토큰 정보 추출
        String accessToken = resolveToken(httpServletRequest);
        System.out.println("==== EXPIERED ACCESSTOKEN : " + accessToken + " ====");

        // 2. 만료된 Access Token 유효성 확인
        if (!jwtTokenProvider.validateExpiredAccessToken(accessToken)) {
            return response.fail("Access Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 3. Access Token 복호화로 추출한 username으로 authentication 객체 만들기
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // 4. Redis 에서 Username을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 5. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 6. 새로운 Access Token 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.regenerateAccessToken(authentication);

        System.out.println("==== NEW ACCESSTOKEN : " + tokenInfo.getAccessToken() + " ====");

        // 7. Response Header에 새 Access Token 세팅
        httpServletResponse.setHeader("Authorization", BEARER_TYPE + " " + tokenInfo.getAccessToken());

//        // 8. SecurityContextHolder에 사용자 정보 넣기
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }




    // 로그아웃
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {

        // 1. Request Header에서 토큰 정보 추출
        String accessToken = resolveToken(httpServletRequest);

        // 2. Access Token 검증
        if (!jwtTokenProvider.validateToken(accessToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 3. Access Token 복호화로 추출한 username으로 authentication 객체 만들기
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // 3. Redis 에서 해당 Username으로 저장된 Refresh Token 이 있는지 여부를 확인 후, 있을 경우 삭제.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
            System.out.println("=== 리프레시 토큰 삭제 완료 ===");
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }


    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest httpServletRequest) {

        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
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
