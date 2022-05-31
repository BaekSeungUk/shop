package com.bitc.shop.repository;

import com.bitc.shop.constant.ItemSellStatus;
import com.bitc.shop.entity.Item;
import com.bitc.shop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item saveItem = itemRepository.save(item);
        System.out.println(saveItem.toString());
    }

    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
        }
    }

    public  void printItemResult(List<Item> itemList) {
        for (Item item : itemList) {
            System.out.println("======================================");
            System.out.println(item.toString());
            System.out.println("======================================");
        }
    }

//    @Test : JUnit를 이용하여 실행하는 test용 메서드 임을 나타내는 어노테이션
    @Test
    @DisplayName("상품 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");

        printItemResult(itemList);
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetail() {
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");

        printItemResult(itemList);
    }

    @Test
    @DisplayName("가격 LessThan 조회 테스트")
    public void findByPriceLessThan() {
        this.createItemList();

        List<Item> itemList = itemRepository.findByPriceLessThan(10005);

        printItemResult(itemList);
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {

        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        printItemResult(itemList);
    }

    @Test
    @DisplayName("@Quary를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");

        printItemResult(itemList);
    }
    
    @Test
    @DisplayName("nativeQuery 속성을 상요한 상품 조회 테스트")
    public void findByItemDetailByNative() {
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");

        printItemResult(itemList);
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        printItemResult(itemList);
    }

}