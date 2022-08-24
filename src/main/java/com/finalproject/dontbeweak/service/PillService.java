package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.PillHistoryRequestDto;
import com.finalproject.dontbeweak.dto.PillHistoryResponseDto;
import com.finalproject.dontbeweak.dto.PillRequestDto;
import com.finalproject.dontbeweak.dto.PillResponseDto;
import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.exception.ErrorCode;
import com.finalproject.dontbeweak.model.Pill;
import com.finalproject.dontbeweak.model.PillHistory;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.PillHistoryRepository;
import com.finalproject.dontbeweak.repository.PillRepository;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PillService {
    private final PillRepository pillRepository;
    private final UserRepository userRepository;
    private final PillHistoryRepository pillHistoryRepository;


    //영양제 등록
    @Transactional
    public PillResponseDto registerPill(PillRequestDto pillRequestDto, UserDetailsImpl userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("회원이 존재하지 않습니다.")
        );

        System.out.println("영양제 생성 중");
        Pill pill = new Pill(user, pillRequestDto);
        System.out.println("영양제 생성 완료");

        System.out.println("영양제 등록 중");
        pillRepository.save(pill);
        System.out.println("영양제 등록 완료");

        return new PillResponseDto(pill);
    }


    //영양제 조회
    public List<PillResponseDto> showPill(@PathVariable String username) {
        List<PillResponseDto> pillResponseDtoList = new ArrayList<>();
        List<Pill> pillList = pillRepository.findByUser_Username(username);

        for (Pill pill : pillList) {
            PillResponseDto pillResponseDto = new PillResponseDto(pill.getProductName(),pill.getCustomColor(), pill.getDone());
            pillResponseDtoList.add(pillResponseDto);
        }
        return pillResponseDtoList;
    }


    //영양제 복용 완료
    public PillHistoryResponseDto donePill(PillHistoryRequestDto pillHistoryRequestDto, UserDetailsImpl userDetails) {

        System.out.println("유저 찾는 중...");
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("회원이 존재하지 않습니다.")
        );
        String productName = pillHistoryRequestDto.getProductName();

        System.out.println("영양제 찾는 중...");
        Pill pill = pillRepository.findByUser_IdAndProductName(user.getId(), productName);
        System.out.println("영양제 찾기 완료");


        pill.donePill();
        System.out.println("영양제 체크, 시간 입력 완료");


        System.out.println("변경 저장하기");
        pillRepository.save(pill);
        System.out.println("변경 저장 완료");

        PillHistory pillHistory = new PillHistory(user, pill, pillHistoryRequestDto);

        pillHistoryRepository.save(pillHistory);

        return new PillHistoryResponseDto(pill, pillHistory);
    }


    //주간 영양제 복용 조회

}

