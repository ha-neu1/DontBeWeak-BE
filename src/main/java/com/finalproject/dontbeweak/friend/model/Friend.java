package com.finalproject.dontbeweak.friend.model;

import com.finalproject.dontbeweak.friend.dto.FriendRequestDto;
import com.finalproject.dontbeweak.login.model.User;
import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@Entity
@AllArgsConstructor
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
    public Friend(String nickname, String friendname, User user, FriendRequestDto requestDto) {
        this.nickname = nickname;
        this.friendname = friendname;
        this.user = user;
    }











}
