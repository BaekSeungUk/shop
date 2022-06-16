package com.bitc.shop.dto;

import com.bitc.shop.entity.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ItemImgDto {
    private Long id;
    private String imgName;
    private String imgUrl;
    private String oriImgName;
    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
