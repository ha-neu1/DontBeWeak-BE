//package com.finalproject.dontbeweak.jwt;
//
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.finalproject.dontbeweak.exception.CustomException;
//import com.finalproject.dontbeweak.exception.ErrorCode;
//import com.finalproject.dontbeweak.model.User;
//import com.finalproject.dontbeweak.auth.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.apache.logging.log4j.message.ExitMessage;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//
//@RequiredArgsConstructor
//public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final JwtService jwtService;
//
//    public FormLoginFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
//        super(authenticationManager);
//        this.jwtService = jwtService;
//    }
//
////    @Value("${secret.key}")
////    private String secretKey;
//
//    //login 요청하면 로그인 시도를 위해 실행되는 함수
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        System.out.println("TRY LOGIN USERNAME & PASSWORD : 인증 검증 _ FormLoginFilter.attemptAuthentication");
//
//        try {
//            ObjectMapper om = new ObjectMapper();
//            User user = om.readValue(request.getInputStream(), User.class);
//
//
//            Authentication authentication =
//                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//
//            return getAuthenticationManager().authenticate(authentication);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    //attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨.
//    //JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해준다.
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        System.out.println("successfulAuthentication 실행됨: 인증이 완료되었다는 뜻.");
//        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
//
//        String accessToken = jwtService.createAccessToken(userDetails.getUsername());
//        String refreshToken = jwtService.createRefreshToken();
//
//        //login 성공 -> refresh 토큰 재발급
//        User userByUsername = jwtService.getUserByUsername(userDetails.getUsername());
//        jwtService.setRefreshTokenToUser(userByUsername, refreshToken);
//
//        jwtService.setResponseOfAccessToken(response, accessToken);
//        jwtService.setResponseOfRefreshToken(response, refreshToken);
//        jwtService.setResponseMessage(true, response, "로그인 성공");
//
//    }
//
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        System.out.println("인증 실패 : unsuccessfulAuthentication");
//        String failMessage = failed.getMessage().equals(ErrorCode.USER_ERROR_NOT_FOUND_ENG.getMessage()) ?
//                                ErrorCode.USER_ERROR_NOT_FOUND.getMessage():
//                                ErrorCode.USER_ERROR_PASSWORD.getMessage();
//        jwtService.setResponseMessage(false,response,"로그인 실패: " + failMessage);
////        response.setStatus(400);
////        response.setCharacterEncoding("UTF-8");
////        response.getWriter().write(failed.getMessage());
//
//
//
//    }
//
////    @Override
////    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
////        System.out.println("인증 실패");
////        String failMessage = failed.getMessage().equals(ErrorCode.USER_ERROR_NOT_FOUND_ENG.getMessage()) ?
////                ErrorCode.USER_ERROR_NOT_FOUND.getMessage():
////                ErrorCode.USER_ERROR_PASSWORD.getMessage();
////        jwtService.setResponseMessage(false, response, "로그인 실패" + ": " + failMessage);
////    }
//}