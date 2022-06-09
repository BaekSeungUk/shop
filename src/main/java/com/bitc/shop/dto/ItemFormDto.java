package com.bitc.shop.dto;

import com.bitc.shop.constant.ItemSellStatus;
import com.bitc.shop.entity.Item;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemFormDto {

    private Long id;

//    @NotBlank : 해당 컬럼의 값 중 빈값을 허용하지 않음
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

//    @NotNull : 해당 컬럼의 값 중 빈값을 허용하지 않음, null만 허용하지 않음, ""인 빈문자열은 허용
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
