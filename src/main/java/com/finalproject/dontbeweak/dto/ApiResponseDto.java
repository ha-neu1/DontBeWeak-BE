package com.finalproject.dontbeweak.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {
    private String PRDUCT;
    private String SRV_USE;
}
