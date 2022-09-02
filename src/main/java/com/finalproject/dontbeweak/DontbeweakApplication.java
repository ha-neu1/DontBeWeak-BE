package com.finalproject.dontbeweak;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class DontbeweakApplication {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application-redis.properties,"
            + "classpath:application.properties,"
            + "classpath:application.yml";

    public static void main(String[] args) {
//        SpringApplication.run(DontbeweakApplication.class, args);
        new SpringApplicationBuilder(DontbeweakApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
