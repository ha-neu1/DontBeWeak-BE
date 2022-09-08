//package com.finalproject.dontbeweak.jwt;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.TokenExpiredException;
//import com.finalproject.dontbeweak.model.User;
//import com.finalproject.dontbeweak.repository.UserRepository;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.Date;
//
//@Service
//@Getter
//@RequiredArgsConstructor
//public class JwtService {
//    private final UserRepository userRepository;
//
//    // refresh token 으로 회원 조회
//    @Transactional(readOnly = true)
//    public User getUserByRefreshToken(String token){
//        return userRepository.findByRefreshToken(token)
//                .orElseThrow(()-> new JwtException(JwtError.JWT_USER_NOT_FOUND_TOKEN));
//    }
//
//    // User 에게 refresh Token 세팅
//    @Transactional
//    public void setRefreshTokenToUser(User user, String token) {
//        userRepository.findByUsername(user.getUsername())
//                .orElseThrow(() -> new JwtException(JwtError.JWT_USER_NOT_FOUND_TOKEN))
//                .setRefreshToken(token);
//    }
//
//    //User refresh 토큰 업데이트
//    @Transactional
//    public String updateRefreshTokenOfUser(User user, String token) {
//        String reissuedRefreshToken = createRefreshToken();
//
//        userRepository.findByRefreshToken(token)
//                .orElseThrow(() -> new JwtException(JwtError.JWT_USER_NOT_FOUND_TOKEN))
//                .setRefreshToken(reissuedRefreshToken);
//
//        return reissuedRefreshToken;
//    }
//
//    @Transactional
//    public void removeRefreshToken(String token){
//        userRepository.findByRefreshToken(token)
//                .orElseThrow(() -> new JwtException(JwtError.JWT_USER_NOT_FOUND_TOKEN))
//                .setRefreshToken(null);
//    }
////    public void logout(HttpServletRequest request){
////        try {
////            checkValidTokenOrThrow(request);
////            String refreshJwtToken = request
////                    .getHeader(JwtProperties.REFRESH_TOKEN_HEADER)
////                    .replace(JwtProperties.TOKEN_PREFIX,"");
////            removeRefreshToken(refreshJwtToken);
////        } catch (Exception e){
////            throw new IllegalArgumentException("Refresh Token이 유효하지 않습니다.");
////        }
////    }
//
//    // Access Token 생성
//    public String createAccessToken(String username){
//        return JWT.create()
//                .withSubject(JwtProperties.ACCESS)
//                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION))
//                .withClaim(JwtProperties.USERNAME, username)
//                .sign(Algorithm.HMAC512("thwjd2"));
//
//    }
//
//    // Refresh Token 생성
//    public String createRefreshToken(){
//        return JWT.create()
//                .withSubject(JwtProperties.REFRESH)
//                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION))
//                .sign(Algorithm.HMAC512("thwjd2"));
//    }
//
//    // 만료 토큰 검증
//    public boolean isNotExpiredToken(String token){
//        try {
//            JWT.require(Algorithm.HMAC512("thwjd2")).build().verify(token);
//        } catch (TokenExpiredException e) {
//            System.out.println("만료된 리프레시 토큰");
//            throw new JwtException(JwtError.JWT_REFRESH_EXPIRED);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//            throw new JwtException(JwtError.JWT_REFRESH_NOT_VALID);
//        }
//        return true;
//    }
//
//    // 1주일 이내 만료 여부 검증
//    public boolean isExpiredInSevenDayTokenOrThrow(String token){
//        try {
//            Date expiresAt = JWT.require(Algorithm.HMAC512("thwjd2"))
//                    .build()
//                    .verify(token)
//                    .getExpiresAt();
//
//            Date current = new Date(System.currentTimeMillis());
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(current);
//            calendar.add(Calendar.DATE, 7);
//
//            Date after7dayFromToday = calendar.getTime();
//            if (expiresAt.before(after7dayFromToday)) {
//                return true;
//            }
//        }catch (TokenExpiredException e){
//            throw new JwtException(JwtError.JWT_REFRESH_EXPIRED);
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//            throw new JwtException(JwtError.JWT_REFRESH_NOT_VALID);
//        }
//        return false;
//    }
//
//    // header 유효성 체크
//    public boolean isValidHeaderOrThrow(HttpServletRequest request){
//        String accessToken = request.getHeader(JwtProperties.ACCESS_TOKEN_HEADER);
//        String refreshToken = request.getHeader(JwtProperties.REFRESH_TOKEN_HEADER);
//
//      if(accessToken != null && refreshToken != null){
//          if(accessToken.startsWith(JwtProperties.TOKEN_PREFIX) && refreshToken.startsWith(JwtProperties.TOKEN_PREFIX)){
//              return true;
//          }
//      }
//      throw new JwtException(JwtError.JWT_HEADER_NOT_VALID);
//    }
//
//    // 토큰 유효성 검증
//    public JwtError checkValidTokenOrThrow(String token) {
//        try {
//            JWT.require(Algorithm.HMAC512("thwjd2"))
//                    .build()
//                    .verify(token);
//            return JwtError.JWT_VALID_TOKEN;
//        } catch (TokenExpiredException e) {
//            return JwtError.JWT_ACCESS_EXPIRED;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new JwtException(JwtError.JWT_ACCESS_NOT_VALID);
//        }
//    }
//
//    public void setResponseOfAccessToken(HttpServletResponse response, String token) {
//        response.addHeader(JwtProperties.ACCESS_TOKEN_HEADER, JwtProperties.TOKEN_PREFIX + token);
//    }
//
//    public void setResponseOfRefreshToken(HttpServletResponse response, String token) {
//        response.addHeader(JwtProperties.REFRESH_TOKEN_HEADER, JwtProperties.TOKEN_PREFIX + token);
//    }
//
//
//    public void setResponseMessage(boolean result, HttpServletResponse response, String message) throws IOException {
//        JSONObject object = new JSONObject();
//        response.setContentType("application/json;charset=UTF-8");
//        if (result) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            object.put("success", true);
//            object.put("code", 1);
//            object.put("message", message);
//        } else {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            object.put("success", false);
//            object.put("code", -1);
//            object.put("message", message);
//        }
//        response.getWriter().print(object);
//    }
//
//
//
//    //리프레시 토큰 추출
//
//    public String extractRefreshToken(HttpServletRequest request) {
//        return request
//                .getHeader(JwtProperties.REFRESH_TOKEN_HEADER)
//                .replace(JwtProperties.TOKEN_PREFIX, "");
//    }
//
//    //엑세스 토큰 추출
//    public String extractAccessToken(HttpServletRequest request) {
//        return request
//                .getHeader(JwtProperties.ACCESS_TOKEN_HEADER)
//                .replace(JwtProperties.TOKEN_PREFIX, "");
//    }
//
//    //username 으로 회원 조회
//    public User getUserByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new JwtException(JwtError.JWT_USER_NOT_FOUND_USERNAME));
//    }
//}
