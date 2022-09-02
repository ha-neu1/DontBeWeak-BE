package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.ApiResponseDto;
import com.finalproject.dontbeweak.model.Api;
import com.finalproject.dontbeweak.repository.ApiRepository;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ApiService {

    private final ApiRepository apiRepository;

    public void parsing(ApiResponseDto apiResponseDto, StringBuilder result) {
        try {
            JSONObject Object;
            //json 객체 생성
            JSONParser jsonParser = new JSONParser();
            //json 파싱 객체 생성
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());

            //데이터 분해
            JSONObject parseResponse = (JSONObject) jsonObject.get("body");
            JSONArray array = (JSONArray) parseResponse.get("items");
            for (int i = 0; i < array.size(); i++) {
                Object = (JSONObject) array.get(i);
                String product = Object.get("PRDUCT").toString();
                String distb_pd = Object.get("DISTB_PD").toString();

                Api api = Api.builder()
                        .DISTB_PD(distb_pd)
                        .PRDUCT(product)
                        .build();
                apiRepository.save(api);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
