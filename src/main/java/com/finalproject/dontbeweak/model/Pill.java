package com.finalproject.dontbeweak.model;

import com.finalproject.dontbeweak.dto.PillDto;
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

    public Pill(User user, PillDto pillDto) {
        this.user = user;
        this.productName = pillDto.getProductName();
        this.customColor = pillDto.getCustomColor();
        this.done = pillDto.isDone();
    }
}
