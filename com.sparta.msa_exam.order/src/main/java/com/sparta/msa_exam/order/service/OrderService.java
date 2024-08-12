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
import org.springframework.cache.annotation.Cacheable;
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


    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order(orderRequestDto.getName());
        orderRepository.save(order);

        List<Long> orderProductIdList = new ArrayList<>();
        for (Long productId : orderRequestDto.getProductIdList()) {
            OrderProduct orderProduct = new OrderProduct(order, productId);
            orderProductRepository.save(orderProduct);
            orderProductIdList.add(orderProduct.getProductId());
        }

        return new OrderResponseDto(order, orderProductIdList);
    }


    @Transactional
    public OrderProductResponseDto addProduct(Long orderId, OrderRequestDto orderRequestDto) {
        Order order = findOrder(orderId);

        List<ProductResponseDto> productList = productClient.getProductList().getBody();
        List<Long> productIdList = new ArrayList<>();

        for (ProductResponseDto product : productList) {
            productIdList.add(product.getId());
        }
        // 주문받은 상품의 ID가 존재 할때만 추가
        for (Long productId : orderRequestDto.getProductIdList()) {
            if (!productIdList.contains(productId)) continue;
            // 주문에 추가된 상품 저장
            orderProductRepository.save(new OrderProduct(order, productId));
        }

        return getOrderList(orderId);
    }


    // 주문 단건 조회 API 캐싱 처리
    @Cacheable(cacheNames = "order-cache", key = "args[0]")
    public OrderProductResponseDto getOrderList(Long orderId) {
        Order order = findOrder(orderId);

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(orderId);
        List<ProductResponseDto> productList = orderProductList.stream()
                .map(orderProduct -> productClient.getProduct(orderProduct.getProductId()))
                .toList();

        return new OrderProductResponseDto(order, productList);
    }


    private Order findOrder(Long id) {

        return orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾으시는 주문이 존재하지 않습니다."));
    }
}