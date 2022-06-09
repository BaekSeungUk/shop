package com.bitc.shop.service;

import com.bitc.shop.dto.ItemFormDto;
import com.bitc.shop.entity.Item;
import com.bitc.shop.entity.ItemImg;
import com.bitc.shop.repository.ItemImgRepository;
import com.bitc.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
}
