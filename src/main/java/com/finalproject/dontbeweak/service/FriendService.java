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

import static com.finalproject.dontbeweak.exception.ErrorCode.FRIEND_CHECKCHECK_CODE;
import static com.finalproject.dontbeweak.exception.ErrorCode.FRIEND_CHECK_CODE;


@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    //친구추가
    @Transactional
    public Friend addfriend(FriendRequestDto friendRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        //로그인 유저 정보.
        User userTemp = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow();

        //등록 이름과 로그인 이름이 같으면. return
        if (userTemp.getUsername().equals(friendRequestDto.getFriendname()))
        throw new CustomException(FRIEND_CHECKCHECK_CODE);

        List<Friend> friend = userTemp.getFriends();
        for(Friend overlapUser : friend) {
            if (overlapUser.getFriendname().equals(friendRequestDto.getFriendname()))
                throw new CustomException(FRIEND_CHECK_CODE);
        }

        Friend newfriend = Friend.builder()
                    .user(userDetails.getUser())
                    .friendname(friendRequestDto.getFriendname())
                    .build();
        friendRepository.save(newfriend);
        return newfriend;
    }

    public List<FriendResponseDto> listfriend() {
        List<Friend> friends = friendRepository.findAll();
        List<FriendResponseDto> responseDtos = new ArrayList<>();
        for(Friend friend: friends){
            FriendResponseDto friendResponseDto = new FriendResponseDto(friend.getFriendname());
            responseDtos.add(friendResponseDto);
        }
        return responseDtos;
    }
}