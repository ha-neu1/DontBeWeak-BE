package com.finalproject.dontbeweak.dto;

import com.finalproject.dontbeweak.model.Item;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemResponseDto {
    private Long id;
    private String itemName;
    private String itemImg;
    private int point;

    public ItemResponseDto(Item item){
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemImg = item.getItemImg();
        this.point = item.getPoint();
    }
}
