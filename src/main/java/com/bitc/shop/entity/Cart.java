package com.bitc.shop.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Data
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @OneToOne : 관계형 데이터 베이스에서 지정한 테이블과 관계 타입을 1:1관계로 설정
//    @JoinColumn : 외례키를 설정, name 속성에 해당 테이블의 기본키명을 입력
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
