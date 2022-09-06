package com.finalproject.dontbeweak.controller;


import com.finalproject.dontbeweak.dto.ApiRequestDto;
import com.finalproject.dontbeweak.service.ApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


@RestController
@AllArgsConstructor
public class ApiController {
    private final ApiService apiService;

    @PostMapping("/api")
    public ResponseEntity<List<ApiRequestDto>> load_save(ApiRequestDto apiRequestDto) throws IOException {

        StringBuilder result = new StringBuilder();

        int pageNumber = 354;
        for (int i = 1; i <= pageNumber; i++) {
            String urla = "http://apis.data.go.kr/1471000/HtfsInfoService2/getHtfsItem?"
                    + "ServiceKey=AEwuEzexgJKaPYcUDyX8Z5ZLxbtExL6%2FnS5eaQp6%2Bq7sD%2BEIyFWTgMwUW1qkvL9ZTs30dx5H1xsZyOzFP9bNyA%3D%3D"
                    + "&numOfRows=" + 99
                    + "&pageNo=" + pageNumber
                    + "&type=json";

            URL url = new URL(urla);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String returnLine;
            while ((returnLine = br.readLine()) != null) {
                result.append(returnLine + "\n\r");
            }
            urlConnection.disconnect();
        }
            List<ApiRequestDto> apiRequestDtos = apiService.parsing(result);


        return ResponseEntity.status(HttpStatus.OK)
                .body(apiRequestDtos);
    }
}










