package com.sparta.msa_exam.order.entity;

import com.sparta.msa_exam.order.dto.OrderRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderItemIds;

    public Order(String name) {
        this.name = name;
    }

    public void updateOrder(List<OrderProduct> orderProductList) {
        this.orderItemIds = orderProductList;
    }
}
