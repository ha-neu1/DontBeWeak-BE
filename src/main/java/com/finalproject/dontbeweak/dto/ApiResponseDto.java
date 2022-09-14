package com.finalproject.dontbeweak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {
    private String product;
    private String entrps;
    private String srv_use;
}
