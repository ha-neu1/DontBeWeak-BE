package com.finalproject.dontbeweak.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemRequestDto {
    private String itemName;
    private String itemImg;
    private int itemPoint;

}
