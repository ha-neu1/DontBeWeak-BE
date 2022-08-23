package com.finalproject.dontbeweak.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ItemHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    private String itemName;

    @Column
    private String username;

    public ItemHistory(User user, Item item) {
        this.user = user;
        this.username = user.getUsername();
        this.item = item;
        this.itemName = item.getItemName();
    }

}
