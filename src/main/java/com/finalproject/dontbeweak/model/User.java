package com.finalproject.dontbeweak.model;

import com.finalproject.dontbeweak.common.BaseEntity;
import com.finalproject.dontbeweak.model.item.ItemHistory;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String nickname;

    @Column
    private String oauth;

    @Column(nullable = false)
    private int point;

    private String role;

    @Column(length = 1000)
    private String refreshToken;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cat> cat;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)//mappedBy 연관관계의 주인이 아니다(나는 FK가 아니에요) DB에 컬럼 만들지 마세요.
    private List<Friend> friends = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ItemHistory> itemHistory;


    @Builder
    public User(String username, String password, String nickname, String oauth, int point, String role, List<Cat> cat, List<Friend> friends, List<ItemHistory> itemHistory) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.oauth = oauth;
        this.point = point;
        this.role = role;
        this.cat = cat;
        this.friends = friends;
        this.itemHistory = itemHistory;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
