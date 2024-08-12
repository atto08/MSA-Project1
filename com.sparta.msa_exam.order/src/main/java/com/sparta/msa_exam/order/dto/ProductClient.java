package com.sparta.msa_exam.order.dto;

import com.sparta.msa_exam.order.service.ProductService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient extends ProductService {

    // 상품 목록 조회
    @GetMapping("/products")
    ResponseEntity<List<ProductResponseDto>> getProductList();
}
