package com.finalproject.dontbeweak.dto;

import com.finalproject.dontbeweak.model.Pill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PillRequestDto {

//    private Long id;
    private String productName;
    private String customColor;
//    private boolean done;
    private LocalDateTime usedAt;

//    public PillRequestDto(Pill pill) {
//        this.id = pill.getUser().getId();
//        this.productName = pill.getProductName();
//        this.customColor = pill.getCustomColor();
//        this.done = pill.getDone();
//        this.usedAt = pill.getUsedAt();
//    }
}
