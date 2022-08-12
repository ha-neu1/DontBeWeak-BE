package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.PillDto;
import com.finalproject.dontbeweak.model.Pill;
import com.finalproject.dontbeweak.repository.PillRepository;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import com.finalproject.dontbeweak.service.PillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PillController {
    private final PillService pillService;
    private final PillRepository pillRepository;

    //영양제 등록
    @PostMapping("/schedule")
    public ResponseEntity<Pill> createPill(@RequestBody List<PillDto> pillDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        pillService.registerPill(pillDto, userDetails);
        return ResponseEntity.ok().body(null);
    }

    //영양제 조회
    @GetMapping("/schedule")
    public ResponseEntity<List<PillDto>> getPill(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PillDto> pillList = pillService.showPill(userDetails);
        return ResponseEntity.ok().body(pillList);
    }
}
