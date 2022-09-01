//package com.finalproject.dontbeweak.config.jwt;
//
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.finalproject.dontbeweak.jwtwithredis.JwtTokenProvider;
//import com.finalproject.dontbeweak.jwtwithredis.UserResponseDto;
//import com.finalproject.dontbeweak.model.User;
//import com.finalproject.dontbeweak.auth.UserDetailsImpl;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.security.Key;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//
//public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private static final String AUTHORITIES_KEY = "auth";
//    private static final String BEARER_TYPE = "Bearer";
//    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;   // 30분
//    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
//    private Key key;
//    private RedisTemplate redisTemplate;
//    private JwtTokenProvider jwtTokenProvider;
//
//
//    public FormLoginFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }
//
////    @Value("${secret.key}")
////    private String secretKey;
//
//    //login 요청하면 로그인 시도를 위해 실행되는 함수
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        System.out.println("1. JwtAuthenticationFilter: 로그인 시도 중");
//
//        try {
//            ObjectMapper om = new ObjectMapper();
//            User user = om.readValue(request.getInputStream(), User.class);
//            System.out.println(user);
//            System.out.println("=============================================");
//
//            // 3. Token 객체 생성 (현 상태는 미검증 authentication)
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//
//            // 4. 3의 Token 객체로 Authentication 객체를 생성
//            Authentication authentication =
//                    getAuthenticationManager().authenticate(authenticationToken);
//
//            return authentication;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    //attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨.
//    //JWT 토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response해주면 됨.
//
//    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//
//        System.out.println("successfulAuthentication 실행됨: 인증이 완료되었다는 뜻.");
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
//
//        //RSA방식은 아니고 Hash암호 방식
////        String jwtToken = JWT.create()
////                .withSubject("cos토큰")
////                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*60)))
////                .withClaim("username",userDetails.getUser().getUsername())
////                .sign(Algorithm.HMAC512("thwjd2"));
//
//
//        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authResult);
//
//        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
//        redisTemplate.opsForValue()
//                .set("RT:" + authResult.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
//
//        response.addHeader("Authorization", BEARER_TYPE +tokenInfo.getAccessToken());
//    }
//
//
//    //로그인 예외처리
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        System.out.println(failed.getMessage());
//        response.setStatus(400);
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(failed.getMessage());
//
//    }
//}