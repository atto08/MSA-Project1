package com.sparta.msa_exam.order.dto;

import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.products.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderProductResponseDto {
    private Long orderId;
    private String name;
    private List<ProductResponseDto> productList;


    public OrderProductResponseDto(Order order, List<ProductResponseDto> productList) {
        this.orderId = order.getId();
        this.name = order.getName();
        this.productList = productList;
    }
}
