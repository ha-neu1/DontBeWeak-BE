package com.finalproject.dontbeweak.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.dontbeweak.cat.service.CatService;
import com.finalproject.dontbeweak.login.dto.SocialLoginInfoDto;
import com.finalproject.dontbeweak.login.model.NaverProfile;
import com.finalproject.dontbeweak.login.model.OAuthToken;
import com.finalproject.dontbeweak.login.model.User;
import com.finalproject.dontbeweak.login.repository.UserRepository;
import com.finalproject.dontbeweak.login.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class NaverService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CatService catService;
    private final UserService userService;

    public SocialLoginInfoDto requestNaver(String code, String state, HttpServletResponse response){

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","k7spc3MRJ9Ut2UUxudqp");
        params.add("client_secret","1feqER4xKL");
        params.add("code",code);
        params.add("client_secret","1feqER4xKL");
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = //바디와 헤더값을 넣어준다
                new HttpEntity<>(params, headers); //아래의 exchange가 HttpEntity 오브젝트를 받게 되어있다.

        //Http요청하기 - Post방식으로 - 그리고 responseEntity 변수의 응답 받음.
        ResponseEntity<String> responseEntity = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(responseEntity.getBody(), OAuthToken.class);
            System.out.println(oauthToken); //oauthToken 값 확인해 보기
        } catch (
                JsonProcessingException e) {
            e.printStackTrace();
        }
        //엑세스 토큰만 뽑아서 확인
        System.out.println("네이버 엑세스 토큰 : " + oauthToken.getAccess_token());


        RestTemplate rt2 = new RestTemplate();

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization","Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> naverProfileRequest2 = //바디와 헤더값을 넣어준다
                new HttpEntity<>(headers2); //아래의 exchange가 HttpEntity 오브젝트를 받게 되어있다.

        //Http요청하기 - Post방식으로 - 그리고 responseEntity 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverProfileRequest2,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        NaverProfile naverProfile = null;
        try {
            naverProfile = objectMapper2.readValue(response2.getBody(), NaverProfile.class);
        } catch (
                JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("네이버 아이디: "+naverProfile.getResponse().getId());
        System.out.println("네이버 닉네임: "+naverProfile.getResponse().getName());
        System.out.println("클라이언트 서버 유저네임 : " + "Naver_" + naverProfile.getResponse().getId());

        User naverUser = User.builder()
                .nickname(naverProfile.getResponse().getName())
                .username("Naver_"+naverProfile.getResponse().getId())
                .password(naverProfile.getResponse().getId())
                .oauth("naver")
                .build();

        User originUser = findByUser(naverUser.getUsername());

        if(originUser.getUsername() == null){
            System.out.println("신규 회원입니다.");
            SignupNaverUser(naverUser);
        }

        // naver 로그인 처리
        System.out.println("naver 로그인 진행중");
        if (naverUser.getUsername() != null) {
            User userEntity = userRepository.findByUsername(naverUser.getUsername()).orElseThrow(
                    () -> new IllegalArgumentException("naver username이 없습니다.")
            );
            UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //홀더에 검증이 완료된 정보 값 넣어준다. -> 이제 controller 에서 @AuthenticationPrincipal UserDetailsImpl userDetails 로 정보를 꺼낼 수 있다.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //JWT 토큰 발급
            String jwtToken = userService.JwtTokenCreate(userDetails.getUser().getUsername());

            response.addHeader("Authorization", jwtToken);
            System.out.println("JWT토큰 : " + "Bearer "+jwtToken);
        }

        String username = naverUser.getUsername();
        String nickname = naverUser.getNickname();

        SocialLoginInfoDto socialLoginInfoDto = new SocialLoginInfoDto(username, nickname);
        return socialLoginInfoDto;

    }

    //신규 네이버 회원 강제 가입
    public String SignupNaverUser(User naverUser) {
        String error = "";
        String username = naverUser.getUsername();
        String password = naverUser.getPassword();
        String nickname = naverUser.getNickname();
        String oauth = naverUser.getOauth();

        // 패스워드 인코딩
        password = passwordEncoder.encode(password);
        naverUser.setPassword(password);

        User user = new User(username, password, oauth, nickname);
        user.setRole("ROLE_USER");
        userRepository.save(user);
        catService.createNewCat(user);

        return error;
    }

    @Transactional(readOnly = true)
    public User findByUser(String username) {
        User user = userRepository.findByUsername(username).orElseGet(
                ()-> {return new User();}
        );
        return user;
    }

}
