package com.finalproject.dontbeweak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //CORS를 적용할 URL패턴을 정의
                .allowedOrigins("*"); //자원 공유를 허락할 Origin을 지정
    }
}
