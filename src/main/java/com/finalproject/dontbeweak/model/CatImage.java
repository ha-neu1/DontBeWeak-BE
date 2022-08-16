package com.finalproject.dontbeweak.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class CatImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String catImage01;

    @Column
    private String catImage10;

    @Column
    private String catImage20;

    @Column
    private String catImage30;
}
