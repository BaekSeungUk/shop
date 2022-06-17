package com.bitc.shop.entity;

import com.bitc.shop.constant.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity{

    @Id
    @Column(name = "order_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true ,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    /*private LocalDateTime regTime;

    private LocalDateTime updateTime;*/

    public void addOrderItem(OrderItem orderItem) {
//        상품 정보를 주문 리스트에 저장
        orderItems.add(orderItem);
//        주문 상품에 주문 정보를 저장
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member); // 주문한 멤버 정보를 설정
        // 주문 상품 목록을 설정
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER); // 주문 상태 설정
        order.setOrderDate(LocalDateTime.now()); // 주문 시간 설정
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

//    지정한 주문의 주문 상태를 CANCEL로 변경하고 주문 상품 리스트의 모든 상품을 주문 취소로 변경함
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
