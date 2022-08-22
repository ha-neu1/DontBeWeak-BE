package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.PillHistoryRequestDto;
import com.finalproject.dontbeweak.dto.PillRequestDto;
import com.finalproject.dontbeweak.dto.PillResponseDto;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import com.finalproject.dontbeweak.service.PillService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> registerPill(@RequestBody PillRequestDto pillRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        pillService.registerPill(pillRequestDto, userDetails);
        return ResponseEntity.ok().body("내 영양제가 등록되었습니다.");
    }

    //영양제 조회
    @GetMapping("/schedule/{username}")
    public ResponseEntity<List<PillResponseDto>> getPill(@PathVariable String username) {
        List<PillResponseDto> pillList = pillService.showPill(username);
        return ResponseEntity.ok().body(pillList);
    }

    //영양제 복용 완료
    @PatchMapping("/schedule/week")
    public ResponseEntity<String> donePill(PillHistoryRequestDto pillHistoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        pillService.donePill(pillHistoryRequestDto, userDetails);
        return ResponseEntity.ok().body("영양제를 잘 챙기셨군요!");
    }

    //주간 영양제 복용 여부 조회
//    @GetMapping("/schedule/{username}/week")
//    public ResponseEntity<List<DonePillResponseDto>> getDone(){
//        List<>
//    }
}
