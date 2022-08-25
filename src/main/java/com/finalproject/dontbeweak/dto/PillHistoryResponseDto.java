package com.finalproject.dontbeweak.dto;

import com.finalproject.dontbeweak.model.Pill;
import com.finalproject.dontbeweak.model.PillHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PillHistoryResponseDto {
    private LocalDateTime usedAt;
    private String productName;
    private String customColor;
    private boolean done;

    public PillHistoryResponseDto(Pill pill, PillHistory pillHistory) {
        this.usedAt = pillHistory.getUsedAt();
        this.productName = pill.getProductName();
        this.customColor = pill.getCustomColor();
        this.done = pill.getDone();
    }
}
