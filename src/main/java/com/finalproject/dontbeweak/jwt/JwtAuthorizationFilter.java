package com.finalproject.dontbeweak.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.auth.UserDetailsImpl;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.PriorityOrdered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    //JWT 토큰이 유효한지 판단하는 필터


    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;

    }
//    @Value("${secret.key}")
//    private String secretKey;

    //인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        //권한이나 인증이 필요한 요청이 전달됨
//        System.out.println("CHECK JWT : 인가(권한) 검증 _ JwtAuthorizationFilter.doFilterInternal");
//
//
//        //헤더 확인
//        String header = request.getHeader("Authorization");
//        if(header == null || !header.startsWith("Bearer ")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        //JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
//        String jwtToken = request.getHeader("Authorization")
//                .replace("Bearer ", "");
//
//        System.out.println("header : "+header);
//
//        String username = null;
//        try{
//            username = JWT
//                    .require(Algorithm.HMAC512("thwjd2"))
//                    .build()
//                    .verify(jwtToken)
//                    .getClaim("username")
//                    .asString();
//        } catch (Exception e){
//            throw new IllegalArgumentException("잘못된 JWT 토큰입니다.");
//        }
//        //서명이 정상적으로 됨
//        if(username != null){
//            User user = userRepository.findByUsername(username).orElseThrow(
//                    ()->new IllegalArgumentException("아이디가 없습니다.")
//            );
//
//            UserDetailsImpl userDetails = new UserDetailsImpl(user);
//
//
//            //JWT 토큰 서명을 통해 서명이 정상이면 Authentication 객체를 만들어준다.
//            Authentication authentication =
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//            //강제로 시큐리티의 세션에 접근하여 Authentication 객체 저장
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            chain.doFilter(request,response);
//        }
//
//    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("CHECK JWT : 인가(권한) 검증 _ JwtAuthorizationFilter.doFilterInternal");
//        try {
//            jwtService.checkHeaderValid(request);
//            String accessJwtToken = request
//                    .getHeader(JwtProperties.ACCESS_TOKEN_HEADER)
//                    .replace(JwtProperties.TOKEN_PREFIX,"");
//            String refreshJwtToken = request
//                    .getHeader(JwtProperties.REFRESH_TOKEN_HEADER)
//                    .replace(JwtProperties.TOKEN_PREFIX,"");
//            jwtService.checkTokenValid(refreshJwtToken);
//
//            System.out.println("리프레시 토큰 회원 조회");
//            User userByRefreshToken = jwtService.getUserByRefreshToken(refreshJwtToken);
//            String username = userByRefreshToken.getUsername();
//
//            //Refresh 토큰이 7일 이내 만료일 경우 Refresh 토큰도 재발급
//            if(jwtService.isNeedToUpdateRefreshToken(refreshJwtToken)){
//                refreshJwtToken = jwtService.createRefreshToken();
//                response.addHeader(JwtProperties.REFRESH_TOKEN_HEADER, JwtProperties.TOKEN_PREFIX + refreshJwtToken);
//                jwtService.setRefreshTokenToUser(username, refreshJwtToken);
//            }
//            try {
//                System.out.println("액세스 토큰 검증");
//                jwtService.checkTokenValid(accessJwtToken);
//            } catch (TokenExpiredException expired){
//                System.out.println("ACCESS TOKEN REISSUE : "+"JWT ACCESS 토큰이 만료되어 재발급합니다.");
//                accessJwtToken  = jwtService.createAccessToken(username);
//                response.addHeader(JwtProperties.ACCESS_TOKEN_HEADER, JwtProperties.TOKEN_PREFIX+accessJwtToken);
//            }
//            UserDetailsImpl userDetails = new UserDetailsImpl(userByRefreshToken);
//            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        } catch (AuthenticationException jwtException){
//            System.out.println("=================================인가 에러=================================");
//            System.out.println(jwtException.getMessage());
//            System.out.println("=================================인가 에러=================================");
//            request.setAttribute(JwtProperties.EXCEPTION, jwtException.getMessage());
//        } catch (Exception e){
//            System.out.println("=================================미정의 에러=================================");
//            System.out.println(e.getMessage());
//            System.out.println("=================================미정의 에러=================================");
//            request.setAttribute(JwtProperties.EXCEPTION,e.getMessage());
//        }
//        chain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인가 / 권한 검증");

        try {
            // 올바르지 않은 헤더는 재로그인
            if (jwtService.isValidHeaderOrThrow(request)) {
                String refreshToken = jwtService.extractRefreshToken(request);
                String accessToken = jwtService.extractAccessToken(request);

                // 만료된 리프레쉬 토큰은 재로그인
                if (jwtService.isNotExpiredToken(refreshToken)) {

                    User userByToken = jwtService.getUserByRefreshToken(refreshToken);

                    // 리프레쉬 토큰이 7일 이내 만료일 경우 새로 발급
                    if (jwtService.isExpiredInSevenDayTokenOrThrow(refreshToken)) {
                        System.out.println("[REFRESH TOKEN] > 리프레쉬 토큰 재발급");
                        refreshToken = jwtService.updateRefreshTokenOfUser(userByToken, refreshToken);
                        jwtService.setResponseOfRefreshToken(response, refreshToken);
                    }

                    // 액세스 토큰이 만료된 경우 새로 발급
                    if (jwtService.checkValidTokenOrThrow(accessToken) == JwtError.JWT_ACCESS_EXPIRED) {
                        System.out.println("[ACCESS TOKEN] > 액세스 토큰 재발급");
                        String reissuedAccessToken = jwtService.createAccessToken(userByToken.getUsername());
                        jwtService.setResponseOfAccessToken(response, reissuedAccessToken);
                    }

                    UserDetailsImpl userDetails = new UserDetailsImpl(userByToken);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            System.out.println("[LOGIN] > 재 로그인 필요");
        } catch (AuthenticationException jwtException) {
            System.out.println("=================================인가 에러=================================");
            System.out.println(jwtException.getMessage());
            System.out.println("=================================인가 에러=================================");
            request.setAttribute(JwtProperties.EXCEPTION, jwtException.getMessage());
        } catch (Exception e) {
            System.out.println("=================================미정의 에러=================================");
            System.out.println(e.getMessage());
            System.out.println("=================================미정의 에러=================================");
            e.printStackTrace();
            request.setAttribute(JwtProperties.EXCEPTION, e.getMessage());
        }

        chain.doFilter(request, response);
    }

}
