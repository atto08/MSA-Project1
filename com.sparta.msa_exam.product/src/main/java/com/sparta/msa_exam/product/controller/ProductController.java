package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.service.ProductService;
import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Value("${server.port}")
    private String port;


    @PostMapping
    public ResponseEntity<Product> createProduct(ProductRequestDto requestDto) {

        Product product = productService.createProduct(requestDto);
        HttpHeaders headers = addPortNumber();
        return new ResponseEntity<>(product, headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProductList() {

        List<ProductResponseDto> productResponseDtoList = productService.getProductList();
        HttpHeaders headers = addPortNumber();
        return new ResponseEntity<>(productResponseDtoList, headers, HttpStatus.OK);
    }

    private HttpHeaders addPortNumber() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("port", port);
        return headers;
    }
}
