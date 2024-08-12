package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.OrderProductResponseDto;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import com.sparta.msa_exam.order.dto.ProductResponseDto;
import com.sparta.msa_exam.order.repository.OrderProductRepository;
import com.sparta.msa_exam.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderProductRepository orderProductRepository;

    private final ProductService productService;


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
        // (Optional 적용 == NPE 방지) - 피드백 적용
        List<ProductResponseDto> productList = Optional.ofNullable(productService.getProductList())
                .map(ResponseEntity::getBody)
                .orElse(Collections.emptyList());

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

        // 서비스 별 클래스 분리 - 피드백 1-2 적용
        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(orderId);
        List<ProductResponseDto> productResponseDtoList = getProductListContainsOrderId(orderProductList);
        return new OrderProductResponseDto(order, productResponseDtoList);
    }


    // 주문 단건 조회 API 캐싱 처리
    @Cacheable(cacheNames = "order-cache", key = "args[0]")
    public OrderProductResponseDto getOrderList(Long orderId) {
        Order order = findOrder(orderId);

        // orderId에 해당하는 OrderProduct 리스트를 조회
        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(orderId);

        // 모든 상품 정보 가져오기 (Optional 적용 == NPE 방지) - 피드백 1-1 적용
        List<ProductResponseDto> productList = getProductListContainsOrderId(orderProductList);

        return new OrderProductResponseDto(order, productList);
    }


    private Order findOrder(Long id) {

        return orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾으시는 주문이 존재하지 않습니다."));
    }

    private List<ProductResponseDto> getProductListContainsOrderId(List<OrderProduct> orderProductList) {
        // 모든 상품 정보 가져오기 (Optional 적용 == NPE 방지) - 피드백 적용 1-1
        List<ProductResponseDto> productAll = Optional.ofNullable(productService.getProductList())
                .map(ResponseEntity::getBody)
                .orElse(Collections.emptyList());

        // orderProductList에 있는 productId에 해당하는 상품 정보만 필터링
        return productAll.stream()
                .filter(product -> orderProductList.stream()
                        .anyMatch(orderProduct -> orderProduct.getProductId().equals(product.getId())))
                .collect(Collectors.toList());
    }
}