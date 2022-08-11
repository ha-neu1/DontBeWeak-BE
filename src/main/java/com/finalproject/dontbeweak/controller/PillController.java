package com.finalproject.dontbeweak.controller;

import com.finalproject.dontbeweak.dto.PillRequestDto;
import com.finalproject.dontbeweak.model.Pill;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.PillRepository;
import com.finalproject.dontbeweak.service.PillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PillController {
    private final PillService pillService;
    private final PillRepository pillRepository;

    //영양제 등록
    @PostMapping("/schedule")
    public ResponseEntity<Pill> registerPill(@PathVariable User username, @RequestBody List<PillRequestDto> pillRequestDto) {
        pillService.registerPill(username, pillRequestDto);
        return ResponseEntity.ok().body();
    }

    //영양제 조회
    @GetMapping("/schedule")
    public ResponseEntity<List<Pill>> getPill(@PathVariable User username) {
        List<Pill> pillList = pillRepository.findAllByPill(username);
        return ResponseEntity.ok().body(pillList);
    }
}
