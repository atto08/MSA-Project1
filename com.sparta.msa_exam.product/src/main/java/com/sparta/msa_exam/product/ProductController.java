package com.sparta.msa_exam.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(ProductRequestDto requestDto){

        return productService.createProduct(requestDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProductList(){

        return productService.getProductList();
    }

    @GetMapping("{id}")
    public ProductResponseDto getProductIdList(@PathVariable Long id){

        return productService.getProductIdList(id);
    }
}
