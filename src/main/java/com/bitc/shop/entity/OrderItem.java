package com.bitc.shop.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

/*    private LocalDateTime regTime;

    private LocalDateTime updateTime;*/

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 판매할 상품 설정
        orderItem.setCount(count); // 판매 상품 수량 설정
        orderItem.setOrderPrice(item.getPrice()); // 판매 상품 총 가격
        item.removeStock(count); // 남은 재고 수량
        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
}
