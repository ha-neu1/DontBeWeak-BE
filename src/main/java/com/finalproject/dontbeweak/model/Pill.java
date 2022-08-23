package com.finalproject.dontbeweak.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finalproject.dontbeweak.dto.PillHistoryRequestDto;
import com.finalproject.dontbeweak.dto.PillRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Pill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String productName;

    //영양제 색상
    @Column(nullable = false)
    private String customColor;

    //영양제 복용 완료 여부
    @Column(nullable = false)
    private Boolean done;

    //복용한 시간
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = true)
    private LocalDateTime usedAt;

    public Pill(User user, PillRequestDto pillRequestDto) {
        this.user = user;
        this.productName = pillRequestDto.getProductName();
        this.customColor = pillRequestDto.getCustomColor();
        this.done = false;
        this.usedAt = pillRequestDto.getUsedAt();
    }

    @Builder
    public void donePill(PillHistoryRequestDto pillHistoryRequestDto) {
        this.done = true;
        this.usedAt = pillHistoryRequestDto.getUsedAt();
    }
}
