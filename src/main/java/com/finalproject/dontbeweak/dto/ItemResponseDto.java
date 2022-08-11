package com.finalproject.dontbeweak.dto;

import com.finalproject.dontbeweak.model.Item;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemResponseDto {
    private Long itemId;
    private String itemName;
    private String itemImg;
    private int point;

    public ItemResponseDto(Item item){
        this.itemId = item.getId();
        this.itemName = item.getItemName();
        this.itemImg = item.getItemImg();
        this.point = item.getPoint();
    }
}
