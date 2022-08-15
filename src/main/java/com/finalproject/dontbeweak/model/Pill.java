package com.finalproject.dontbeweak.model;

import com.finalproject.dontbeweak.dto.PillRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(nullable = false)
    private String customColor;

    @Column(nullable = false)
    private Boolean done = false;

    public Pill(User user, PillRequestDto pillRequestDto) {
        this.user = user;
        this.productName = pillRequestDto.getProductName();
        this.customColor = pillRequestDto.getCustomColor();
        this.done = pillRequestDto.isDone();
    }
}
