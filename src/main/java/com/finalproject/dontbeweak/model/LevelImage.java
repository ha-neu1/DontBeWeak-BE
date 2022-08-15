package com.finalproject.dontbeweak.model;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Table(name = "level_image")
public class LevelImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
//    private int changeLevel;

    @Column(nullable = false)
    private String catImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "levelImage")
    private List<Cat> cat;
}
