package com.bitc.shop.repository;

import com.bitc.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

//    쿼리 메서드로 데이터베이스에서 데이터를 가져옴
//    itemId와 repImgYn을 검색 조건으로 데이터를 가져옴
    ItemImg findByIdAndRepImgYn(Long itemId, String reqImgYn);
}
