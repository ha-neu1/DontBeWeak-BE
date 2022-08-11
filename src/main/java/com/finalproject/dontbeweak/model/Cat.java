package com.finalproject.dontbeweak.model;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int exp;

    @Column(nullable = false)
    private int maxExp;

    @Column(nullable = false)
    private String catImage;

    // 최대 레벨
    public static final Integer MAX_LEVEL = 30;
    // 최소 레벨
    public static final Integer MIN_LEVEL = 1;

    // 최초 고양이 생성
    public Cat(User user) {
        this.user = user;
        this.level = 1;
        this.exp = 0;
        this.maxExp = 20;
        this.catImage = new CatLevelUpImage().getCatImgVer01();
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    // 경험치 상승
//    public boolean plusExp() {
//        int level = getLevel();
//        int exp = getExp();
//        int maxExp = getMaxExp();
//        int addExp = 5;
//
//        // 현재 레벨이 최대 레벨보다 낮을 때
//        if (level < MAX_LEVEL) {
//            if ((exp + addExp) < maxExp) {
//                setExp(exp + addExp);
//            } else {
//                ((exp + addExp) - maxExp)
//            }
//        }
//
//    }

    // 레벨업
}
