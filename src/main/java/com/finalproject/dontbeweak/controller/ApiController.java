package com.finalproject.dontbeweak.controller;
import com.finalproject.dontbeweak.model.Api;
import com.finalproject.dontbeweak.repository.ApiRepository;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@AllArgsConstructor
public class ApiController {

    private final ApiRepository apiRepository;

    @GetMapping("/api")
    public String load_save() throws IOException,NullPointerException {
//        RL(String protocol, String host, int port, String file) : 프로토콜 식별자 protocol, 호스트 주소 host, 포트 번호 port, 파일 이름 file이 지정하는 자원에 대한 URL 객체 생성
//        생성RL(String protocol, String host, intnt port, String file) : 프로토콜 식별자 protocol, 호스트 주소 host, 포트 번호 port, 파일 이름 file이 지정하는 자원에 대한 URL 객체 생성
        StringBuilder result = new StringBuilder();
        try {
            String urla = "http://apis.data.go.kr/1471000/HtfsInfoService2/getHtfsList?ServiceKey=AEwuEzexgJKaPYcUDyX8Z5ZLxbtExL6%2FnS5eaQp6%2Bq7sD%2BEIyFWTgMwUW1qkvL9ZTs30dx5H1xsZyOzFP9bNyA%3D%3D&numOfRows=99&pageNo=2&type=json";
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

            JSONObject Object;
            //json 객체 생성
            JSONParser jsonParser = new JSONParser();
            //json 파싱 객체 생성
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());

//           //데이터 분해
            JSONObject parseResponse = (JSONObject) jsonObject.get("body");

            JSONArray array = (JSONArray)parseResponse.get("items");
            for (int i = 0; i < array.size(); i++) {
                Object=(JSONObject)array.get(i);



                String entrps =Object.get("ENTRPS").toString();
                String product = Object.get("PRDUCT").toString();
                String redist = Object.get("REGIST_DT").toString();
                String stt = Object.get("STTEMNT_NO").toString();

                Api api = Api.builder()
                        .ENTRPS(entrps)
                        .PRDUCT(product)
                        .REGIST_DT(redist)
                        .STTEMNT_NO(stt)
                        .build();
                apiRepository.save(api);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        return result.toString();

    }


}