package com.finalproject.dontbeweak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemRequestDto {
    private Long id;
    private String itemName;
    private String itemImg;
    private int point;

}
