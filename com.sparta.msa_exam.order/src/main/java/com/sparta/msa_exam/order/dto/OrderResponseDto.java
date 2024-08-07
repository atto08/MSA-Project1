package com.sparta.msa_exam.order.dto;

import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private String name;
    private List<Long> productIdList;

    public OrderResponseDto(Order updateOrder) {
        this.id = updateOrder.getId();
        this.name = updateOrder.getName();
        this.productIdList = new ArrayList<>();
        for (OrderProduct productId : updateOrder.getOrderItemIds()){
            productIdList.add(productId.getProductId());
        }
    }

    public OrderResponseDto(Order order, List<Long> orderProductList) {
        this.id = order.getId();
        this.name = order.getName();
        this.productIdList = orderProductList;
    }
}
