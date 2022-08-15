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
    public ResponseEntity<String> registerPill(@RequestBody PillDto pillDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        pillService.registerPill(pillDto, userDetails);
        return ResponseEntity.ok().body("내 영양제가 등록되었습니다.");
    }

    //영양제 조회
    @GetMapping("/schedule")
    public ResponseEntity<List<Pill>> getPill() {
        List<Pill> pillList = pillRepository.findAll();
        return ResponseEntity.ok().body(pillList);
    }
}
