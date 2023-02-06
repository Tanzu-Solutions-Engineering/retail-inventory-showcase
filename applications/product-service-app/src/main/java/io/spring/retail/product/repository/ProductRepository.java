package io.spring.retail.product.repository;

import io.spring.retail.product.domain.Product;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface ProductRepository extends GemfireRepository<Product,String> {
}
