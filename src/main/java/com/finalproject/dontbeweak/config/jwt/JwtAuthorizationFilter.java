//package com.finalproject.dontbeweak.config.jwt;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.finalproject.dontbeweak.model.User;
//import com.finalproject.dontbeweak.repository.UserRepository;
//import com.finalproject.dontbeweak.auth.UserDetailsImpl;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
//
//    private UserRepository userRepository;
//
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
//        super(authenticationManager);
//        this.userRepository = userRepository;
//
//    }
//    @Value("${secret.key}")
//    private String secretKey;
//
//    //인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        System.out.println("인증이나 권한이 필요한 주소 요청이 됨");
//
////        String jwtHeader = request.getHeader("Authorization");
////        System.out.println("jwtHeader : " + jwtHeader);
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
//
//        //JWT토큰을 검증을 해서 정상적인 사용자인지 확인
//        String username =
//                JWT.require(Algorithm.HMAC512(secretKey)).build().verify(jwtToken).getClaim("username").asString();
//
//
//        //서명이 정상적으로 됨
//        if(username != null){
//            User user = userRepository.findByUsername(username).orElseThrow(
//                    ()->new IllegalArgumentException("아이디가 없습니다.")
//            );
//
//            UserDetailsImpl userDetails = new UserDetailsImpl(user);
//
//            //JWT 토큰 서명을 통해 서명이 정상이면 Authentication 객체를 만들어준다. (인증된 Authentication 객체로 token이 setAuthenticated(true)이다)
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
//}
