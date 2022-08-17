package com.finalproject.dontbeweak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DonePillRequestDto {
    private LocalDateTime dateTime;
    private boolean done;
}
