package com.sparta.msa_exam.order.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String name;
    private Integer supply_price;
}
