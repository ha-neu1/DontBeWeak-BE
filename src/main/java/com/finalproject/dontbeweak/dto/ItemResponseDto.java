package com.finalproject.dontbeweak.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ItemResponseDto {
    private List<ItemRequestDto> itemList;
}
