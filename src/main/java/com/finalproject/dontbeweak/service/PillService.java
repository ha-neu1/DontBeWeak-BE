package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.PillRequestDto;
import com.finalproject.dontbeweak.dto.PillResponseDto;
import com.finalproject.dontbeweak.model.Pill;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.PillRepository;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PillService {
    private final PillRepository pillRepository;
    private final UserRepository userRepository;

    //영양제 등록
    @Transactional
    public void registerPill(PillRequestDto pillRequestDto, UserDetailsImpl userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("회원이 존재하지 않습니다.")
        );

        Pill pill = new Pill(user, pillRequestDto);
        pillRepository.save(pill);
        new PillRequestDto(pill);
    }

    //영양제 조회
    public List<PillResponseDto> showPill(UserDetailsImpl userDetails) {
        List<PillResponseDto> pillResponseDtoList = new ArrayList<>();
        List<Pill> pillList = pillRepository.findAllByUser_Id(userDetails.getUser().getId());

        for (Pill pill : pillList) {
            PillResponseDto pillResponseDto = new PillResponseDto(pill.getProductName(),pill.getCustomColor(), pill.getDone());
            pillResponseDtoList.add(pillResponseDto);
        }
        return pillResponseDtoList;
    }
}

