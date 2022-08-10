package com.finalproject.dontbeweak.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "catId")
    private Cat cat;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private String itemImg;
}

