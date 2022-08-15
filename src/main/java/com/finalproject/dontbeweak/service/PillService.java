package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.PillDto;
import com.finalproject.dontbeweak.model.Pill;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.PillRepository;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PillService {
    private final PillRepository pillRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registerPill(PillDto pillDto, UserDetailsImpl userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("회원이 존재하지 않습니다.")
        );

//        Pill pill = pillRepository.findByUser_Id(user.getId());
        Pill pill = new Pill(user, pillDto);
        pillRepository.save(pill);
        new PillDto(pill);
    }


}
