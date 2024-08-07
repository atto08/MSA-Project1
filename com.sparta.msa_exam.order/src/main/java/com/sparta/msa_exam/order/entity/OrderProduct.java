package com.sparta.msa_exam.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orderProduct")
@Getter
@Setter
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


    public OrderProduct(Order order, Long productId) {
        this.order = order;
        this.productId = productId;
    }
}
