package com.finalproject.dontbeweak.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finalproject.dontbeweak.model.Pill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
//@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PillHistoryRequestDto {

    private String productName;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime usedAt;

    private boolean done;

}
