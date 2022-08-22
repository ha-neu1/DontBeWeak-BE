package com.finalproject.dontbeweak.dto;

import com.finalproject.dontbeweak.model.Cat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginIdCheckDto {
    private String username;
    private String nickname;
    private int point;
    private int level;
    private int exp;



}