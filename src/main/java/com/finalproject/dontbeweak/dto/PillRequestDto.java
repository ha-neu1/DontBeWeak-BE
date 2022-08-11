package com.finalproject.dontbeweak.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PillRequestDto {
    private String productName;
    private String color;
}
