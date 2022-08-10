package com.finalproject.dontbeweak.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ItemRequestDto {
    private Long id;
    private String itemName;
    private String itemImg;
    private int point;

}
