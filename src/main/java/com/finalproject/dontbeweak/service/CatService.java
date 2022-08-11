package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.CatResponseDto;
import com.finalproject.dontbeweak.model.Cat;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CatService {

    private final CatRepository catRepository;

    // 새 고양이 생성
    @Transactional
    public void createNewCat(User user) {
        Cat cat = new Cat(user);
        catRepository.save(cat);
    }


    // 내 고양이 조회
    @Transactional
    public CatResponseDto getMyCatStatus(UserDetails userDetails) {

        String username = userDetails.getUsername();
        Cat cat = catRepository.findByUser_Username(username).orElseThrow(() -> new IllegalArgumentException("아이디가 없습니다."));

        return new CatResponseDto(cat);
    }

}
