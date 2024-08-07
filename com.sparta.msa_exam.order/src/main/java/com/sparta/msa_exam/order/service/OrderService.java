package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.OrderProductResponseDto;
import com.sparta.msa_exam.order.products.ProductClient;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import com.sparta.msa_exam.order.products.ProductResponseDto;
import com.sparta.msa_exam.order.repository.OrderProductRepository;
import com.sparta.msa_exam.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderProductRepository orderProductRepository;

    private final ProductClient productClient;

    @Value("${server.port}")
    private String port;


    public ResponseEntity<OrderResponseDto> createOrder(OrderRequestDto orderRequestDto) {

        Order order = new Order(orderRequestDto.getName());
        orderRepository.save(order);

        List<Long> orderProductIdList = new ArrayList<>();
        for (Long productId : orderRequestDto.getProductIdList()){
            OrderProduct orderProduct = new OrderProduct(order, productId);
            orderProductRepository.save(orderProduct);
            orderProductIdList.add(orderProduct.getProductId());
        }
        OrderResponseDto orderResponseDto = new OrderResponseDto(order, orderProductIdList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("port", port);

        return new ResponseEntity<>(orderResponseDto, headers, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<OrderResponseDto> addProduct(Long orderId, OrderRequestDto orderRequestDto) {

        Order order = findOrder(orderId);

        List<ProductResponseDto> productList = productClient.getProductList().getBody();
        List<Long> productIdList = new ArrayList<>();

        for (ProductResponseDto product : productList){
            productIdList.add(product.getId());
        }

        for(Long productId : orderRequestDto.getProductIdList()){
            if(!productIdList.contains(productId)) continue;

            orderProductRepository.save(new OrderProduct(order, productId));
        }
        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(orderId);
        order.updateOrder(orderProductList);

        Order updateOrder = orderRepository.save(order);
        OrderResponseDto orderResponseDto = new OrderResponseDto(updateOrder);

        HttpHeaders headers = new HttpHeaders();
        headers.add("port", port);

        return new ResponseEntity<>(orderResponseDto, headers, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<OrderProductResponseDto> getOrderList(Long orderId) {
        Order order = findOrder(orderId);

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(orderId); // 해당 주문에 관련된 모든 상품 Id List
        // product-service 에서 해당 상품 정보 리스트 전환
        List<ProductResponseDto> productList = orderProductList.stream()
                .map(orderProduct -> productClient.getProduct(orderProduct.getProductId())).toList();

        OrderProductResponseDto orderProductResponseDto = new OrderProductResponseDto(order, productList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("port", port);

        return new ResponseEntity<>(orderProductResponseDto, headers, HttpStatus.OK);
    }


    private Order findOrder(Long id) {

        return orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾으시는 주문이 존재하지 않습니다."));
    }
}
