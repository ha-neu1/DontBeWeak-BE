package com.finalproject.dontbeweak.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finalproject.dontbeweak.dto.ItemRequestDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String itemImg;

    @Column(nullable = false)
    private int point;
    public Item(ItemRequestDto itemRequestDto, User user, Cat cat){
        this.user = user;
        this.cat = cat;
        this.itemName = itemRequestDto.getItemName();
        this.itemImg = itemRequestDto.getItemImg();
        this.point = itemRequestDto.getPoint();
    }
}

