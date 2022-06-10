package com.bitc.shop.service;

import com.bitc.shop.dto.ItemFormDto;
import com.bitc.shop.dto.ItemImgDto;
import com.bitc.shop.dto.ItemSearchDto;
import com.bitc.shop.entity.Item;
import com.bitc.shop.entity.ItemImg;
import com.bitc.shop.repository.ItemImgRepository;
import com.bitc.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemImgService itemImgService;
    @Autowired
    ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        for (int i = 0; i < itemImgFileList.size(); i++) {

//            상품 이미지 리스트에서 실제로 존재하는 이미지 정보만 데이터 베이스에 저장
            if (itemImgFileList.get(i).isEmpty()) {
                continue;
            }

            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0) {
                itemImg.setRepImgYn("Y");
            }
            else {
                itemImg.setRepImgYn("N");
            }

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return  item.getId();
    }

//    @Transactional(readOnly = true) : 읽기 전용으로 사용하여 성능 향상
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
//        실제 데이터 베이스에서 이미지 정보를 가져옴
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        
//        이미지 정보를 ItemImgDto 클래스 타입의 List 객체 타입으로 변환
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

//        데이터 베이스에 저장된 정보는 Item 엔티티 클래스 타입이고,. View에서 사용된 데이터 타입은 ItemFormDto 클래스 타입이므로 해당 타입으로 데이터 타입을 변환
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgIds(itemImgDtoList);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws  Exception {
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIdList();

        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return  item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }
}
