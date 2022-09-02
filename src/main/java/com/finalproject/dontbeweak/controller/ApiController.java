package com.finalproject.dontbeweak.controller;


import com.finalproject.dontbeweak.dto.ApiResponseDto;
import com.finalproject.dontbeweak.service.ApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/api")
    public ResponseEntity<List<ApiResponseDto>> load_save(ApiResponseDto apiResponseDto) throws IOException{

        StringBuilder result = new StringBuilder();

            int pagenumber = 3;
            String urla = "http://apis.data.go.kr/1471000/HtfsInfoService2/getHtfsItem?"
                    + "ServiceKey=AEwuEzexgJKaPYcUDyX8Z5ZLxbtExL6%2FnS5eaQp6%2Bq7sD%2BEIyFWTgMwUW1qkvL9ZTs30dx5H1xsZyOzFP9bNyA%3D%3D" // 서비스키
                    + "&numOfRows=" + 99
                    + "&pageNo=" + pagenumber
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

            List<ApiResponseDto> friendResponseDtoList = apiService.parsing(apiResponseDto,result);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(friendResponseDtoList);
    }
}










