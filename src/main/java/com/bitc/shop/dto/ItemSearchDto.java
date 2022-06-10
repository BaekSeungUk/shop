package com.bitc.shop.dto;

import com.bitc.shop.constant.ItemSellStatus;
import lombok.Data;

@Data
public class ItemSearchDto {
    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private String searchBy;
    private String searchQuery = "";
}
