package com.finalproject.dontbeweak.controller;


import com.finalproject.dontbeweak.dto.ApiResponseDto;
import com.finalproject.dontbeweak.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class ApiController {
    private final ApiService apiService;
    public static final int pageSize = 8;

    //공공API 데이터 DB 저장
    @GetMapping("/api")
    public ResponseEntity<String> load_save() throws IOException, NullPointerException {
        apiService.parsing();
        return ResponseEntity.status(HttpStatus.OK)
                .body("공공 데이터가 담겼습니다");
    }

    // 영양제 조회 및 검색
    @GetMapping("/api/search")
    public ResponseEntity<Slice<ApiResponseDto>> searchProducts(@RequestParam(value = "product", required = false) String product, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = pageSize) Pageable pageNo){
        Page<ApiResponseDto> products = apiService.searchProducts(product,pageNo);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

}










