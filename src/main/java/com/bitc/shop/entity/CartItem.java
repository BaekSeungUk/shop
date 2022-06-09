package com.bitc.shop.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cart_item")
@Data
public class CartItem extends BaseEntity {
    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "count")
    private int count;

}
