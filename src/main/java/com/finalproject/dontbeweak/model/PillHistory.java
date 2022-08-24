package com.finalproject.dontbeweak.model;

import com.finalproject.dontbeweak.dto.PillHistoryRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class PillHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pill_id")
    private Pill pill;

    //영양제 이름
    @Column(nullable = false)
    private String productName;

    //복용한 시간
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = true)
    private LocalDateTime usedAt;

    public PillHistory(User user, Pill pill, PillHistoryRequestDto pillHistoryRequestDto) {
        this.user = user;
        this.pill = pill;
        this.productName = pill.getProductName();
        this.usedAt = pillHistoryRequestDto.getUsedAt();
    }
}
