package com.finalproject.dontbeweak.model;

import com.finalproject.dontbeweak.dto.CatRequestDto;
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
        this.level = MIN_LEVEL;
        this.exp = 0;
        this.maxExp = 20;
        this.catImage = new CatLevelUpImage().getCatImgVer01();
    }

//    @Builder
//    public Cat(long id, User user, int level, int exp, int maxExp, String catImage) {
//        this.id = id;
//        this.user = user;
//        this.level = level;
//        this.exp = exp;
//        this.maxExp = maxExp;
//        this.catImage = catImage;
//    }


    public void setExp(int exp) {
        this.exp = exp;
    }


    // 레벨 설정
    public void setLevel(int level) {
        if (level <= MAX_LEVEL) {
            this.level = level;
        }
         setCatImage(level);
    }

    // 10 레벨마다 고양이 이미지 바뀜
    public void setCatImage(int level) {
//        int level = getLevel();

        if (level == 10) {
            this.catImage = new CatLevelUpImage().getCatImgVer02();
        } else if (level == 20) {
            this.catImage = new CatLevelUpImage().getCatImgVer03();
        } else if (level == 30) {
            this.catImage = new CatLevelUpImage().getCatImgVer04();
        }
    }

    // 경험치 상승 및 레벨 업
    public void addExpAndLevel() {
        int level = getLevel();
        int exp = getExp();
        int maxExp = getMaxExp();
        int addExp = 5;

        // 현재 레벨이 최대 레벨보다 낮을 때
        if (level < MAX_LEVEL) {
            if ((exp + addExp) < maxExp) {  // 원래 경험치 + 추가된 경험치가 최대 경험치보다 적을 때,
                setExp(exp + addExp);       // 경험치 상승.
            } else {                        // 원래 경험치 + 추가된 경험치가 최대 경험치 이상일 때,
                setLevel(level + 1);        // 레벨 1 상승 후,
                setExp((exp + addExp) - maxExp);    // 레벨업 후 남은 경험치를 설정.
            }
        // 현재 레벨이 최대 레벨일 때
        } else {
            if ((exp + addExp) < maxExp) {  // 원래 경험치 + 추가된 경험치가 최대 경험치보다 적을 때,
                setExp(exp + addExp);       // 경험치 상승.
            } else {                        // 원래 경험치 + 추가된 경험치가 최대 경험치 이상일 때,
                setExp(maxExp);             // 최대 경험치로 설정.
            }
        }
    }

    public void changeExpLevel(CatRequestDto requestDto) {
        this.exp = requestDto.getExp();
        this.level = requestDto.getLevel();
    }
}
