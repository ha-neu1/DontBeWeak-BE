package com.finalproject.dontbeweak.dto;

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
}
