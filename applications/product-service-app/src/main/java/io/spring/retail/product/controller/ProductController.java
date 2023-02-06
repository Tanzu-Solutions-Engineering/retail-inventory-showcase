package io.spring.retail.product.controller;

import io.spring.retail.product.domain.Product;
import io.spring.retail.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @MutationMapping
    public Product saveProduct(@Argument String id,@Argument String name)
    {
        var product = Product.builder()
                .id(id)
                .name(name)
                .build();

        productRepository.save(product);
        return product;
    }

    @QueryMapping
    public Iterable<Product> products(@Argument int count, @Argument int offset)
    {
        return productRepository.findAll();
    }
}
