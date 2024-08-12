package com.sparta.msa_exam.product;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    @Transactional
    @CacheEvict(cacheNames = "productList-cache", allEntries = true)
    public Product createProduct(ProductRequestDto requestDto) {

        Product product = new Product(requestDto);
        productRepository.save(product);
        return product;
    }

    @Cacheable(cacheNames = "productList-cache")
    public List<ProductResponseDto> getProductList() {

        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for (Product product : productList) {
            productResponseDtoList.add(new ProductResponseDto(product));
        }

        return productResponseDtoList;
    }

    public ProductResponseDto getProductIdList(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return new ProductResponseDto(product);
    }
}
