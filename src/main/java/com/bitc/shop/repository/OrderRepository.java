package com.bitc.shop.repository;

import com.bitc.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

//    사용자 email을 기준으로 주문 목록을 내림차순으로 가져옴
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

//    사용자 email을 기준으로 주문 목록의 수를 가져옴
    @Query("select count(o) from Order o " +
            "where o.member.email = :email")
    Long countOrder(@Param("email") String email);
}
