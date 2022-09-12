package com.finalproject.dontbeweak.controller;


import com.finalproject.dontbeweak.dto.ApiResponseDto;
import com.finalproject.dontbeweak.model.Api;
import com.finalproject.dontbeweak.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ApiController {
    private final ApiService apiService;
    public static final int pageSize = 7;

    //공공API 데이터 DB 저장
    @GetMapping("/api")
    public ResponseEntity<String> load_save() throws IOException, NullPointerException {
        apiService.parsing();
        return ResponseEntity.status(HttpStatus.OK)
                .body("공공 데이터가 담겼습니다");
    }

    // 모든 영양제 목록을 보여주는 컨트롤러
    @GetMapping("/apiList")
    public List<ApiResponseDto> api(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = pageSize) Pageable pageable){
        // 응답 list 객체 생성
        List<ApiResponseDto> data = new ArrayList<>();
        Page<Api> list = apiService.api(pageable);
        for (int i = 0; i < pageSize; i++){
            data.add(new ApiResponseDto(list.getContent().get(i)));
        }
        System.out.println(data);
        return data;
    }

    // 무한스크롤 발생시 반응하는 컨트롤러
    @GetMapping("/apiList/infinity")
    public List<ApiResponseDto> apiInfinity(@RequestParam String product,
                                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = pageSize) Pageable pageable){

        // 응답 list 객체 생성
        List<ApiResponseDto> data = new ArrayList<>();
        Page<Api> list = apiService.apiInfinity(product,pageable);
        for (int i = 0; i < pageSize; i++){
            data.add(new ApiResponseDto(list.getContent().get(i)));
        }
        return data;
    }

}










