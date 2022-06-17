package com.bitc.shop.service;

import com.bitc.shop.dto.OrderDto;
import com.bitc.shop.dto.OrderHistDto;
import com.bitc.shop.dto.OrderItemDto;
import com.bitc.shop.entity.*;
import com.bitc.shop.repository.ItemImgRepository;
import com.bitc.shop.repository.ItemRepository;
import com.bitc.shop.repository.MemberRepository;
import com.bitc.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        // 지정한 상품이 있는지 검색
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

//        사용자 정보가 DB에서 조회하여 가져옴
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

//        사용자 정보와 주문할 상품 목록을 사용하여 Order 객체를 생성
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order); // 생성된 주문 정보 DB에 저장

        return order.getId();
    }

    //
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHisDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHisDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHisDtos, pageable, totalCount);
    }

//    현재 사용자와 주문자가 같은지 확인
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
//        현재 사용자 정보를 email을 통해서 가져옴
        Member curMember = memberRepository.findByEmail(email);
//        주문id를 통해서 주문 정보를 가져옴
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
//        주문 정보에 포함된 사용자 정보를 가져옴
        Member savedMember = order.getMember();

//        현재 사용자 정보와 주문 정보에 포함된 사용자 정보가 같은지 확인
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return  true;
    }

//    실제 주문 취소 시작 부분
    public  void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }
}
