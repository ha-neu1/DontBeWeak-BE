package com.finalproject.dontbeweak.model;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class CatLevelImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private String changeLevelImage;

    @OneToMany(mappedBy = "catLevelImage")
    private List<Cat> cat;


    private final String catImgVer01 = "https://hyerim-bucket.s3.ap-northeast-2.amazonaws.com/static/cat001.jpg";

    private final String catImgVer02 = "https://hyerim-bucket.s3.ap-northeast-2.amazonaws.com/static/ray-zhuang-Px2Y-sio6-c-unsplash-scaled.jpg";

    private final String catImgVer03 = "https://hyerim-bucket.s3.ap-northeast-2.amazonaws.com/static/3636_10174_4958.jpg";

    private final String catImgVer04 = "https://hyerim-bucket.s3.ap-northeast-2.amazonaws.com/static/cat-3449999_1920.jpg";

    public boolean getLevel(int level) {
        if (level == this.level) {
            return true;
        } else {
            return false;
        }
    }
}

/*

public enum LevelUpImage {
    CATIMGVER01(1, "https://hyerim-bucket.s3.ap-northeast-2.amazonaws.com/static/cat001.jpg"),
    CATIMGVER02(10, "https://hyerim-bucket.s3.ap-northeast-2.amazonaws.com/static/ray-zhuang-Px2Y-sio6-c-unsplash-scaled.jpg"),
    CATIMGVER03(20, "https://hyerim-bucket.s3.ap-northeast-2.amazonaws.com/static/3636_10174_4958.jpg"),
    CATIMGVER04(30, "https://hyerim-bucket.s3.ap-northeast-2.amazonaws.com/static/cat-3449999_1920.jpg");

    private final int level;
    private final String catImage;

    LevelUpImage(int level, String catImage) {
        this.level = level;
        this.catImage = catImage;
    }
}
*/
