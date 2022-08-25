package com.finalproject.dontbeweak.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finalproject.dontbeweak.model.Pill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PillHistoryRequestDto {

    private String productName;

    @NotNull(message = "시간 값은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime usedAt;

    private boolean done;

}
