package com.sparta.msa_exam.order.dto;

import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto implements Serializable {
    private Long id;
    private String name;
    private List<Long> productIdList;

    public OrderResponseDto(Order updateOrder) {
        this.id = updateOrder.getId();
        this.name = updateOrder.getName();
        this.productIdList = new ArrayList<>();
        for (OrderProduct productId : updateOrder.getOrderItemIds()) {
            productIdList.add(productId.getProductId());
        }
    }

    public OrderResponseDto(Order order, List<Long> orderProductList) {
        this.id = order.getId();
        this.name = order.getName();
        this.productIdList = orderProductList;
    }
}
