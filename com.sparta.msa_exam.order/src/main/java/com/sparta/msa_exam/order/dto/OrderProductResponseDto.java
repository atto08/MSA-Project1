package com.sparta.msa_exam.order.dto;

import com.sparta.msa_exam.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductResponseDto implements Serializable {
    private Long orderId;
    private String name;
    private List<ProductResponseDto> productList;


    public OrderProductResponseDto(Order order, List<ProductResponseDto> productList) {
        this.orderId = order.getId();
        this.name = order.getName();
        this.productList = productList;
    }
}
