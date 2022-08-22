package com.finalproject.dontbeweak.dto;

import com.finalproject.dontbeweak.model.Pill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PillHistoryRequestDto {
    private String productName;
    private LocalDateTime usedAt;
    private boolean done;

    public PillHistoryRequestDto(Pill pill) {
        this.productName = pill.getProductName();
        this.done = pill.getDone();
        this.usedAt = pill.getUsedAt();
    }
}
