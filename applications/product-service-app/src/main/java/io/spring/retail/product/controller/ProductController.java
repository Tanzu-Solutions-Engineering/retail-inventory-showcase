package io.spring.retail.product.controller;

import com.vmware.retail.product.domain.Nutrition;
import com.vmware.retail.product.domain.Product;
import io.spring.retail.product.repository.ProductGemFireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductGemFireRepository productRepository;

    @MutationMapping
    public Product saveProduct(@Argument String id,
                               @Argument String name,
                               @Argument Double price,
                               @Argument String details,
                               @Argument String ingredients,
                               @Argument String directions,
                               @Argument String warnings,
                               @Argument String quantityAmount,
                               @Argument Integer totalFatAmount,
                                @Argument Integer cholesterol,
                                @Argument Integer sodium,
                                @Argument Integer totalCarbohydrate,
                                @Argument Integer sugars,
                                @Argument Integer protein,
                                @Argument Integer calories)
    {
       return save(Product.builder()
               .id(id)
               .name(name)
               .price(price)
               .details(details)
               .ingredients(ingredients)
               .directions(directions)
               .warnings(warnings)
               .quantityAmount(quantityAmount)
               .nutrition(Nutrition.builder()
                       .totalFatAmount(totalFatAmount)
                       .cholesterol(cholesterol)
                       .sodium(sodium)
                       .totalCarbohydrate(totalCarbohydrate)
                       .sugars(sugars)
                       .protein(protein)
                       .calories(calories)
                       .build())
               .build());
    }


    public Product save(Product product)
    {
        productRepository.save(product);
        return product;
    }

    @QueryMapping
    public Iterable<Product> products(@Argument int count, @Argument int offset)
    {
        return productRepository.findAll();
    }

    @QueryMapping
    public Iterable<Product> queryProducts( @Argument String query)
    {
        return productRepository.queryProducts(query);
    }
}
