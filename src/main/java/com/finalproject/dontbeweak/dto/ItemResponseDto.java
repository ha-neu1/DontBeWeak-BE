package com.finalproject.dontbeweak.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemResponseDto {
    private List<ItemRequestDto> itemList;
}
