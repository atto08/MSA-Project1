package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.products.ProductResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<List<ProductResponseDto>> getProductList();
}
