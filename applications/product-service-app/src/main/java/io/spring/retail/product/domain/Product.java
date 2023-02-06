package io.spring.retail.product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String id;
    private String name;
    private Double price;
    private String details;
    private String ingredients;
    private String directions;
    private String warnings;
    private String quantityAmount;
    private Nutrition nutrition;
}
