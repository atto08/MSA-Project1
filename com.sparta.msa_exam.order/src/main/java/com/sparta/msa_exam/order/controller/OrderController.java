package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.OrderProductResponseDto;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto){

        return orderService.createOrder(orderRequestDto);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> addProduct(@PathVariable Long orderId,
                                                        @RequestBody OrderRequestDto orderRequestDto){

        return orderService.addProduct(orderId, orderRequestDto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderProductResponseDto> getOrderList(@PathVariable Long orderId){

        return orderService.getOrderList(orderId);
    }
}
