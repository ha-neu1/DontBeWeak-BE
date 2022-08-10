package com.finalproject.dontbeweak.service;

import com.finalproject.dontbeweak.dto.ItemRequestDto;
import com.finalproject.dontbeweak.model.Item;
import com.finalproject.dontbeweak.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Setter
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    //아이템 등록
    @Transactional
    public void inputItem(ItemRequestDto requestDto){
        String itemName = requestDto.getItemName();
        String itemImg = requestDto.getItemImg();
        int point = requestDto.getPoint();

        Item item = Item.builder()
                .itemName(itemName)
                .itemImg(itemImg)
                .point(point)
                .build();

        itemRepository.save(item);




    }
}
