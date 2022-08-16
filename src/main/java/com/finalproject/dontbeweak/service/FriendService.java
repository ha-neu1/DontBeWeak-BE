package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.FriendRequestDto;
import com.finalproject.dontbeweak.dto.FriendResponseDto;
import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.model.Friend;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.FriendRepository;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.finalproject.dontbeweak.exception.ErrorCode.FRIEND_ADD_CODE;
import static com.finalproject.dontbeweak.exception.ErrorCode.FRIEND_CHECK_CODE;


@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    //친구 추가
    @Transactional
    public Friend addfriend(FriendRequestDto friendRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //유저에 없는 사람을 추가 못하도록 함
        User friend = userRepository.findUserByUsername(friendRequestDto.getFriendname())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다"));

        String nickname = friend.getNickname();

        //자기 자신을 추가 못하게 함
        User userTemp = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow();
        if (userTemp.getUsername().equals(friendRequestDto.getFriendname()))
            throw new CustomException(FRIEND_ADD_CODE);

        //이미 등록된 친구를 친구추가 할 수 없도록 함
        List<Friend> friends =userTemp.getFriends();
        for(Friend overlapUser : friends) {
            if (overlapUser.getFriendname().equals(friendRequestDto.getFriendname()))
                throw new CustomException(FRIEND_CHECK_CODE);
        }

        Friend newFriend = Friend.builder()
                .user(userDetails.getUser())
                .friendname(friendRequestDto.getFriendname())
                .nickname(nickname)
                .build();
        friendRepository.save(newFriend);
        return newFriend;

    }

    //친구 목록 조회
    public List<FriendResponseDto> listfriend() {
        List<Friend> friends = friendRepository.findAll();
        List<FriendResponseDto> responseDtos = new ArrayList<>();
        for(Friend friend: friends){
            FriendResponseDto friendResponseDto = new FriendResponseDto(friend.getNickname());
            responseDtos.add(friendResponseDto);
        }
        return responseDtos;
    }
}