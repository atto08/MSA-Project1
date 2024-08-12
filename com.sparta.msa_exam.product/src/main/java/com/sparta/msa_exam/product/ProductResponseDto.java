package com.sparta.msa_exam.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto implements Serializable {
    private Long id;
    private String name;
    private Integer supply_price;


    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.supply_price = product.getSupply_price();
    }
}
