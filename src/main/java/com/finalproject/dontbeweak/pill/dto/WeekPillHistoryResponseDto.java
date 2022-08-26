package com.finalproject.dontbeweak.pill.dto;

import com.finalproject.dontbeweak.pill.model.PillHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeekPillHistoryResponseDto {
    private LocalDateTime usedAt;
    private int dayOfWeekValue;
    private String productName;
    private String customColor;
    private boolean done;

    public WeekPillHistoryResponseDto(PillHistory pillHistory, int dayOfWeekValue) {
        this.usedAt = pillHistory.getUsedAt();
        this.dayOfWeekValue = dayOfWeekValue;
        this.productName = pillHistory.getPill().getProductName();
        this.customColor = pillHistory.getPill().getCustomColor();
        this.done = pillHistory.getPill().getDone();
    }
}
