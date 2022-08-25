package com.finalproject.dontbeweak.login.model;

import com.finalproject.dontbeweak.cat.model.Cat;
import com.finalproject.dontbeweak.common.baseEntity.BaseEntity;
import com.finalproject.dontbeweak.friend.model.Friend;
import com.finalproject.dontbeweak.item.model.ItemHistory;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cat> cat;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)//mappedBy 연관관계의 주인이 아니다(나는 FK가 아니에요) DB에 컬럼 만들지 마세요.
    private List<Friend> friends = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ItemHistory> itemHistory;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public User(Long id, String username, String nickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.friends = getFriends();
    }

    public User(String username, String password, String oauth, String nickname) {
        this.username = username;
        this.password = password;
        this.oauth = oauth;
        this.nickname = nickname;
    }
}
