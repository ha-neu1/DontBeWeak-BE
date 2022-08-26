package com.finalproject.dontbeweak.pill.service;

import com.finalproject.dontbeweak.pill.dto.PillHistoryRequestDto;
import com.finalproject.dontbeweak.pill.dto.PillHistoryResponseDto;
import com.finalproject.dontbeweak.pill.dto.PillRequestDto;
import com.finalproject.dontbeweak.pill.dto.PillResponseDto;
import com.finalproject.dontbeweak.pill.model.Pill;
import com.finalproject.dontbeweak.pill.model.PillHistory;
import com.finalproject.dontbeweak.login.model.User;
import com.finalproject.dontbeweak.pill.repository.PillHistoryRepository;
import com.finalproject.dontbeweak.pill.repository.PillRepository;
import com.finalproject.dontbeweak.login.repository.UserRepository;
import com.finalproject.dontbeweak.login.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        Optional<Pill> found = pillRepository.findPillByProductName(pillRequestDto.getProductName());

        if (found.isPresent()) {
            throw new IllegalArgumentException("이미 같은 이름의 영양제가 존재합니다.");
        }

        Pill pill = new Pill(user, pillRequestDto);
        pillRepository.save(pill);

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
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("회원이 존재하지 않습니다.")
        );

        String productName = pillHistoryRequestDto.getProductName();

        Pill pill = pillRepository.findByUser_IdAndProductName(user.getId(), productName);
        pill.donePill();
        pillRepository.save(pill);

        PillHistory pillHistory = new PillHistory(user, pill, pillHistoryRequestDto);
        pillHistoryRepository.save(pillHistory);

        return new PillHistoryResponseDto(pill, pillHistory);
    }

    //영양제 복용 여부 초기화
    @Transactional
    public Long update(Long id, boolean done){
        Pill pill = pillRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("영양제가 존재하지 않습니다.")
        );

        pill.reset(done);
        return id;
    }

    //주간 영양제 복용 조회
    @Transactional
    public List<PillHistoryResponseDto> getPillList(String username, String startDate, String endDate) {
        Optional<User> user = userRepository.findByUsername(username);
        Long userId = user.get().getId();

        LocalDateTime startDateTime = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(0, 0, 0);
        LocalDateTime endDateTime = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(23, 59, 59);

        List<PillHistory> pillHistoryList = pillHistoryRepository.findAllByUser_IdAndUsedAtBetween(userId, startDateTime, endDateTime);
        List<PillHistoryResponseDto> pillHistoryResponseDtoList = new ArrayList<>();

        for (PillHistory pillHistory : pillHistoryList) {

            PillHistoryResponseDto pillHistoryResponseDto = new PillHistoryResponseDto(pillHistory.getUsedAt(), pillHistory.getUsedAt().getDayOfWeek().getValue(), pillHistory.getPill().getProductName(),pillHistory.getPill().getCustomColor(), pillHistory.getPill().getDone());

            pillHistoryResponseDtoList.add(pillHistoryResponseDto);
        }

        return pillHistoryResponseDtoList;
    }

}

