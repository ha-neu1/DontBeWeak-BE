package com.finalproject.dontbeweak.model;

import com.finalproject.dontbeweak.dto.PillRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
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
    private Boolean done = false;

    //복용한 시간
    @Column(nullable = false)
    private LocalDateTime usedAt;

    public Pill(User user, PillRequestDto pillRequestDto) {
        this.user = user;
        this.productName = pillRequestDto.getProductName();
        this.customColor = pillRequestDto.getCustomColor();
        this.done = pillRequestDto.isDone();
        this.usedAt = pillRequestDto.getUsedAt();
    }

    public void done() {
        this.done = true;
    }

    public void usedAt() {
        this.usedAt = getUsedAt();
    }
}
