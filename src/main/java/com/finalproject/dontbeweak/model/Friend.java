package com.finalproject.dontbeweak.model;

import com.finalproject.dontbeweak.dto.FriendRequestDto;
import lombok.*;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nickname;

    @Column
    private String friendname;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Friend(String nickname, String friendname, User user) {
        this.nickname = nickname;
        this.friendname = friendname;
        this.user = user;
    }











}
