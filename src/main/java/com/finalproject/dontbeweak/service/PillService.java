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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PillService {
    private final UserRepository userRepository;
    private final PillRepository pillRepository;

    @Transactional
    public void registerPill(List<PillDto> pillDto, UserDetailsImpl userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다")
        );

        HashSet<String> hashSet = new HashSet<>();
        for(PillDto pillDtoList : pillDto){
            hashSet.add(pillDtoList.getProductName());
            hashSet.add(pillDtoList.getCustomColor());
        }

        if(hashSet.size() != pillDto.size())
            throw new IllegalArgumentException("이미 등록되었습니다.");
    }

    public List<PillDto> showPill(UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다")
        );

        List<PillDto> list = new ArrayList<>();
        List<Pill> pillList = pillRepository.findAllByPill(user);

        for (Pill pill : pillList) {
            PillDto pillDto = new PillDto(pill.getProductName(), pill.getCustomColor());
            list.add(pillDto);
        }
        return list;
    }
}
