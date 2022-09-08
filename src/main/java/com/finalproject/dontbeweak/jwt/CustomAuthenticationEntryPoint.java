//package com.finalproject.dontbeweak.jwt;
//
//import lombok.RequiredArgsConstructor;
//import org.json.JSONObject;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.naming.AuthenticationException;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    private final JwtService jwtService;
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
//        String exceptionMessage = (String) request.getAttribute(JwtProperties.EXCEPTION);
//        System.out.println("Exception : "+exceptionMessage);
//        System.out.println("authException message: "+authException.getMessage());
//        jwtService.setResponseMessage(false, response, exceptionMessage);
//
//    }
//
////    private void setResponse(HttpServletResponse response, String message) throws IOException{
////        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////        response.setContentType("application/json;charset=UTF-8");
////
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("success",false);
////        jsonObject.put("code",-1);
////        jsonObject.put("message",message);
////
////        response.getWriter().print(jsonObject);
////    }
//}
