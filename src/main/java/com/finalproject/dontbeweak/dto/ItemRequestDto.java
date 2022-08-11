package com.finalproject.dontbeweak.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemRequestDto {
    private String itemName;
    private String itemImg;
    private int point;

}
