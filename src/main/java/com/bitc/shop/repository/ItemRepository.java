package com.bitc.shop.repository;

import com.bitc.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

//    아래의 쿼리문에서 i는 엔티티인 item 클래스 타입의 객체 변수 임
//    select i from Item i > select * from item 와 같음
//    :itemDetail - 아래의 메서드에서 @Param 어노테이션을 사용하여 지정한 변수를 뜻함
//    이름 기반으로 하여 변수를 쿼리문에 적용함
//    ?1, ?2, ... - 아래의 메서드에서 @Param 어노테이션을 사용하지 않을 경우 순서를 기반으로 해서 매개변수를 쿼리문에 적용하는 방식, 소스코드 및 쿼리문 변경 시 잘못된 결과가 출력될 수 있음 ?1부터 시작
//    @Query("select  i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
//    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
@Query("select  i from Item i where i.itemDetail like %?1% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

@Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
