package com.bitc.shop.repository;

import com.bitc.shop.dto.ItemSearchDto;
import com.bitc.shop.dto.MainItemDto;
import com.bitc.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
