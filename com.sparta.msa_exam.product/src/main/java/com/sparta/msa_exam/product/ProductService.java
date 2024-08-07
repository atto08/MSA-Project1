package com.sparta.msa_exam.product;

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
public class ProductService {

    private final ProductRepository productRepository;

    @Value("${server.port}")
    private String port;

    public ResponseEntity<Product> createProduct(ProductRequestDto requestDto) {

        Product product = new Product(requestDto);
        productRepository.save(product);

        HttpHeaders headers = new HttpHeaders();
        headers.add("port", port);

        return new ResponseEntity<>(product, headers, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<List<ProductResponseDto>> getProductList() {

        System.out.println("Now port : " + port);
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for (Product product : productList) {
            productResponseDtoList.add(new ProductResponseDto(product));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("port", port);

        return new ResponseEntity<>(productResponseDtoList, headers, HttpStatus.OK);
    }

    public ProductResponseDto getProductIdList(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return new ProductResponseDto(product);
    }
}
