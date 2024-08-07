package com.sparta.msa_exam.order.products;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    // 상품 개별 조회
    @GetMapping("/products/{id}")
    ProductResponseDto getProduct(@PathVariable("id") Long id);

    // 상품 목록 조회
    @GetMapping("/products")
    ResponseEntity<List<ProductResponseDto>> getProductList();
}
