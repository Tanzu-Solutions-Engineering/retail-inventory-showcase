package io.spring.retail.product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Nutrition {
    private Integer totalFatAmount;
    private Integer cholesterol;
    private Integer sodium;
    private Integer totalCarbohydrate;
    private Integer sugars;
    private Integer protein;
    private Integer calories;
}
