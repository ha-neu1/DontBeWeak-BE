package com.finalproject.dontbeweak.service;


import com.finalproject.dontbeweak.dto.BuyItemResponseDto;
import com.finalproject.dontbeweak.dto.ItemRequestDto;
import com.finalproject.dontbeweak.dto.ItemResponseDto;
import com.finalproject.dontbeweak.exception.CustomException;
import com.finalproject.dontbeweak.exception.ErrorCode;
import com.finalproject.dontbeweak.model.Cat;
import com.finalproject.dontbeweak.model.Item;
import com.finalproject.dontbeweak.model.ItemHistory;
import com.finalproject.dontbeweak.model.User;
import com.finalproject.dontbeweak.repository.CatRepository;
import com.finalproject.dontbeweak.repository.ItemHistoryRepository;
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
    private final CatRepository catRepository;
    private final CatService catService;
    private final ItemHistoryRepository itemHistoryRepository;



    //아이템 등록
    @Transactional
    public void inputItem(ItemRequestDto requestDto) throws IOException {
        String itemName = requestDto.getItemName();
        String itemImg = requestDto.getItemImg();
        int itemPoint = requestDto.getItemPoint();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .itemName(itemName)
                .itemImg(itemImg)
                .itemPoint(itemPoint)
                .build();

        Item item = new Item(itemRequestDto);
        itemRepository.save(item);

    }


    //아이템 목록 조회
    public List<ItemResponseDto> getItem(){

        List<Item> items = itemRepository.findAll();
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        for(Item item : items) {
            ItemResponseDto itemResponseDto = new ItemResponseDto(item);
            itemResponseDtoList.add(itemResponseDto);
        }
        return itemResponseDtoList;
    }

    //아이템 구입 및 적용
    @Transactional
    public BuyItemResponseDto patchItem(Long itemId, UserDetailsImpl userDetails) {

        Long userId = userDetails.getUser().getId();
        User user = getUser(userId);
        Item item = findItem(itemId);
        String username = userDetails.getUsername();




        //포인트 감소, 고양이 경험치 상승
        if(user.getPoint() >= item.getItemPoint()){
            int newPoint = user.getPoint() - item.getItemPoint();
            user.setPoint(newPoint);

            // 아이템 구입 기록 저장
            ItemHistory itemHistory = new ItemHistory(user, item);
            itemHistoryRepository.save(itemHistory);

            Cat cat = catRepository.findByUser_Username(username).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CAT));
            catService.addExp(cat);

            return BuyItemResponseDto.builder()
                    .username(user.getUsername())
                    .itemName(item.getItemName())
                    .itemImg(item.getItemImg())
                    .point(user.getPoint())
                    .build();
        } else {
            throw new CustomException(ErrorCode.NOT_ENOUGH_MONEY);
        }
    }


    //member 찾기
    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    //아이템 찾기
    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NO_ITEM));
    }
}