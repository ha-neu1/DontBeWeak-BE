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

    private String productName;
    private String customColor;
    private LocalDateTime usedAt;

}
