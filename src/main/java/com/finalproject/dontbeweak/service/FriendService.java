package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.FriendRequestDto;
import com.finalproject.dontbeweak.dto.FriendResponseDto;
import com.finalproject.dontbeweak.model.Friend;
import com.finalproject.dontbeweak.repository.FriendRepository;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;




@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    //친구추가
    @Transactional
    public void addfriend(FriendRequestDto friendRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        //친구추가하기
        Friend newfriend = Friend.builder()
                .user(userDetails.getUser())
                .friendname(friendRequestDto.getFriendname())
                .build();
        friendRepository.save(newfriend);

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