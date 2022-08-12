package com.finalproject.dontbeweak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BuyItemResponseDto {
    private Long userName;
    private String itemName;
    private String itemImg;
    private int point;

}
