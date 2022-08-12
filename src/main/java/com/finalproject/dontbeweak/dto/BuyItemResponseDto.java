package com.finalproject.dontbeweak.dto;

import com.finalproject.dontbeweak.model.Item;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BuyItemResponseDto {

    private String userName;
    private String itemName;
    private String itemImg;
    private int point;

    public BuyItemResponseDto(Item item, UserDetailsImpl userDetails) {
        this.userName = userDetails.getUsername();
        this.itemName = item.getItemName();
        this.itemImg = item.getItemImg();
        this.point = userDetails.getUser().getPoint();
    }
}
