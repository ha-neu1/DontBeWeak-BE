package com.finalproject.dontbeweak.pill.controller;


import com.finalproject.dontbeweak.login.auth.UserDetailsImpl;
import com.finalproject.dontbeweak.login.exception.CustomException;
import com.finalproject.dontbeweak.login.exception.ErrorCode;
import com.finalproject.dontbeweak.pill.dto.PillHistoryRequestDto;
import com.finalproject.dontbeweak.pill.dto.PillHistoryResponseDto;
import com.finalproject.dontbeweak.pill.dto.PillRequestDto;
import com.finalproject.dontbeweak.pill.dto.PillResponseDto;
import com.finalproject.dontbeweak.pill.service.PillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PillController {
    private final PillService pillService;

    //영양제 등록
    @PostMapping("/schedule")
    public ResponseEntity<PillResponseDto> registerPill(@RequestBody PillRequestDto pillRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PillResponseDto pill = pillService.registerPill(pillRequestDto, userDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pill);
    }

    //영양제 조회
    @GetMapping("/schedule/{username}")
    public ResponseEntity<List<PillResponseDto>> getPill(@PathVariable String username) {
        List<PillResponseDto> pillList = pillService.showPill(username);
        return ResponseEntity.ok().body(pillList);
    }

    //영양제 복용 완료
    @PatchMapping("/schedule/week")
    public ResponseEntity<PillHistoryResponseDto> donePill(@RequestBody PillHistoryRequestDto pillHistoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        } else {
            PillHistoryResponseDto pillHistoryResponseDto = pillService.donePill(pillHistoryRequestDto, userDetails);
            return ResponseEntity.ok().body(pillHistoryResponseDto);
        }
    }

    //주간 영양제 복용 여부 조회
    @GetMapping("/schedule/{username}/week")
    public ResponseEntity<List<PillHistoryResponseDto>> getPillHistory
    (@PathVariable String username,
     @RequestParam(value = "startDate", required = false) String startDate,
     @RequestParam(value = "endDate", required = false) String endDate){

        List<PillHistoryResponseDto> pillHistoryResponseDtoList = pillService.getPillList(username, startDate, endDate);

        return ResponseEntity.ok().body(pillHistoryResponseDtoList);
    }
}
