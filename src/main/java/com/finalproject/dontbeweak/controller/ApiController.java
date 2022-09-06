package com.finalproject.dontbeweak.controller;


import com.finalproject.dontbeweak.service.ApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@AllArgsConstructor
public class ApiController {
    private final ApiService apiService;

    //공공API 데이터 DB 저장
    @GetMapping("/api")
    public ResponseEntity<String> load_save() throws IOException, NullPointerException {
        apiService.parsing();
        return ResponseEntity.status(HttpStatus.OK)
                .body("공공 데이터가 담겼습니다");
    }

}










