package com.finalproject.dontbeweak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DonePillResponseDto {
    private LocalDate date;
    private String productName;
    private String customColor;
    private boolean done;
}
