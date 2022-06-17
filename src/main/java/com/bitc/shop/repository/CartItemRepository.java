package com.bitc.shop.repository;

import com.bitc.shop.dto.CartDetailDto;
import com.bitc.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.bitc.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl)"+
    "from CartItem ci, ItemImg im " +
    "join ci.item i " +
    "where ci.cart.id = :cartId "  +
    "and im.item.id = ci.item.id " +
    "and im.repImgYn = 'Y' " +
    "order by ci.regTime desc "
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
