package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.model.Api;
import com.finalproject.dontbeweak.repository.ApiRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class ApiService {

    private final ApiRepository apiRepository;

    //공공API 데이터 DB 저장
    public String parsing() throws IOException, NullPointerException {
        StringBuilder result = null;

        for (int pageNumber = 1; pageNumber <= 354; pageNumber++) {
            String urla = "http://apis.data.go.kr/1471000/HtfsInfoService2/getHtfsItem?"
                    + "ServiceKey=AEwuEzexgJKaPYcUDyX8Z5ZLxbtExL6%2FnS5eaQp6%2Bq7sD%2BEIyFWTgMwUW1qkvL9ZTs30dx5H1xsZyOzFP9bNyA%3D%3D"
                    + "&numOfRows=" + 99
                    + "&pageNo=" + pageNumber
                    + "&type=json";

            URL url = new URL(urla);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(30000);
            urlConnection.setRequestMethod("GET");
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String returnLine;
            result = new StringBuilder();
            ArrayList<StringBuilder> urls = new ArrayList<>();
            while ((returnLine = br.readLine()) != null) {
                result.append(returnLine + "\n\r");
            }
            urls.add(result);
            urlConnection.disconnect();

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

                    String entrps = (String) Object.get("ENTRPS");
                    if (entrps == null) {
                        entrps = "";
                    }

                    String product = (String) Object.get("PRDUCT");
                    if (product == null) {
                        product = "";
                    }

                    String srv_use = (String) Object.get("SRV_USE");
                    if (srv_use == null) {
                        srv_use = "";
                    }

                    Api api = Api.builder()
                            .ENTRPS(entrps)
                            .PRDUCT(product)
                            .SRV_USE(srv_use)
                            .build();
                    apiRepository.save(api);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    // 모든 영양제 목록 조회
    @Transactional
    public Page<Api> api(Pageable pageNo) {
        return apiRepository.findAll(pageNo);
    }

    // 무한스크롤 발생시 반응하는 목록 조회
    @Transactional
    public Page<Api> apiInfinity(String product, Pageable pageNo) {
        return apiRepository.findAllByPRDUCTLessThan(product, pageNo);
    }
}
