package com.finalproject.dontbeweak.model;

import com.finalproject.dontbeweak.dto.ItemRequestDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CAT_ID")
    private Cat cat;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String itemImg;

    @Column(nullable = false)
    private int point;

    public Item(ItemRequestDto itemRequestDto){
        this.user = user;
        this.cat = cat;
        this.itemName = itemRequestDto.getItemName();
        this.itemImg = itemRequestDto.getItemImg();
        this.point = itemRequestDto.getPoint();
    }
}

