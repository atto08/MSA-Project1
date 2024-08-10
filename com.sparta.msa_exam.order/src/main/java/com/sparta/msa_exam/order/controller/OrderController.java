package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.OrderProductResponseDto;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Value("${server.port}")
    private String port;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {

        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto);
        HttpHeaders headers = addPortNumber();
        return new ResponseEntity<>(orderResponseDto, headers, HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> addProduct(@PathVariable Long orderId,
                                                       @RequestBody OrderRequestDto orderRequestDto) {

        OrderResponseDto orderResponseDto = orderService.addProduct(orderId, orderRequestDto);
        HttpHeaders headers = addPortNumber();
        return new ResponseEntity<>(orderResponseDto, headers, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderProductResponseDto> getOrderList(@PathVariable Long orderId) {

        OrderProductResponseDto orderProductResponseDto = orderService.getOrderList(orderId);
        HttpHeaders headers = addPortNumber();
        return new ResponseEntity<>(orderProductResponseDto, headers, HttpStatus.OK);
    }

    private HttpHeaders addPortNumber() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("port", port);
        return headers;
    }
}
