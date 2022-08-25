package com.finalproject.dontbeweak.cat.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class CatImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int changeLevel;

    @Column
    private String catImage;

}
