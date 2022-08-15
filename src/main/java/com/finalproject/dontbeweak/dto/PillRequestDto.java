package com.finalproject.dontbeweak.dto;

import com.finalproject.dontbeweak.model.Pill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PillRequestDto {
    private Long id;
    private String productName;
    private String customColor;
    private boolean done;

    public PillRequestDto(Pill pill) {
        this.id = pill.getUser().getId();
        this.productName = pill.getProductName();
        this.customColor = pill.getCustomColor();
        this.done = pill.getDone();
    }
}
