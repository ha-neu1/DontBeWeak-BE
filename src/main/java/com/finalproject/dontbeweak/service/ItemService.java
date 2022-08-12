package com.finalproject.dontbeweak.service;


import com.finalproject.dontbeweak.dto.ItemRequestDto;
import com.finalproject.dontbeweak.dto.ItemResponseDto;
import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.exception.ErrorCode;
import com.finalproject.dontbeweak.model.Cat;
import com.finalproject.dontbeweak.model.Item;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.ItemRepository;
import com.finalproject.dontbeweak.repository.UserRepository;
import com.finalproject.dontbeweak.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Setter
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    private final UserRepository userRepository;


    //아이템 등록
    @Transactional
    public void inputItem(ItemRequestDto itemRequestDto) throws IOException {
        String itemName = itemRequestDto.getItemName();
        String itemImg = itemRequestDto.getItemImg();
        int itemPoint = itemRequestDto.getItemPoint();

        Item item = Item.builder()
                .itemName(itemName)
                .itemImg(itemImg)
                .itemPoint(itemPoint)
                .build();

        itemRepository.save(item);

    }


    //아이템 목록 조회
    public List<ItemResponseDto> getItem(UserDetailsImpl userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new NullPointerException("회원이 존재하지 않습니다.")
        );

        List<Item> items = itemRepository.findAll();
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for(Item item : items) {
            ItemResponseDto itemResponseDto = new ItemResponseDto(item);
            itemResponseDtoList.add(itemResponseDto);
        }
        return itemResponseDtoList;
    }

    //아이템 구입
    //유저포인트 - 아이템포인트 = 남은 포인트  => 포인트 깎여야함
    @Transactional
    public void buyItem(Long itemId, UserDetailsImpl userDetails){

        Item item = itemRepository.findItemById(itemId);
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                ()->new IllegalArgumentException("회원이 존재하지 않습니다.")
        );
        if(user.getPoint() >= item.getItemPoint()){
            int newPoint = user.getPoint() - item.getItemPoint();
            user.setPoint(newPoint);
        } else {
           throw new CustomException(ErrorCode.NOT_ENOUGH_MONEY);
        }




////
////        User user = new User();
////        Item item = new Item();
////        int nowPoint = user.getPoint();
////        int itemPoint = item.getPoint();
////        User savedUser = userRepository.save(user);
////        if(nowPoint >= itemPoint){
////            int newPoint = nowPoint - itemPoint;
////            savedUser.setPoint(newPoint);
////        } else {
////            throw new CustomException(ErrorCode.NOT_ENOUGH_MONEY);
//        }


    }
}
