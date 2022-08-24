package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.PillHistoryRequestDto;
import com.finalproject.dontbeweak.dto.PillHistoryResponseDto;
import com.finalproject.dontbeweak.dto.PillRequestDto;
import com.finalproject.dontbeweak.dto.PillResponseDto;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import com.finalproject.dontbeweak.service.PillService;
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
    @PatchMapping("/schedule/{username}")
    public ResponseEntity<PillHistoryResponseDto> donePill(@RequestBody PillHistoryRequestDto pillHistoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        PillHistoryResponseDto pillHistoryResponseDto = pillService.donePill(pillHistoryRequestDto, userDetails);
        return ResponseEntity.ok().body(pillHistoryResponseDto);
    }

    //주간 영양제 복용 여부 조회
//    @GetMapping("/schedule/{username}/week")
//    public ResponseEntity<List<DonePillResponseDto>> getDone(){
//        List<>
//    }
}
