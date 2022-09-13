package com.finalproject.dontbeweak.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.finalproject.dontbeweak.model.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {
    private String product;
    private String entrps;
    private String srv_use;

    public ApiResponseDto(Api api){
        this.product = api.getPRDUCT();
        this.entrps = api.getENTRPS();
        this.srv_use = api.getSRV_USE();
    }
}
