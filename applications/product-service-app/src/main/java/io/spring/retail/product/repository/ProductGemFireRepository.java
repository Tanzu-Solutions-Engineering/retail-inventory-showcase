package io.spring.retail.product.repository;

import com.vmware.retail.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductGemFireRepository {
    private final ProductRepository repository;
    private final GemfireTemplate template;

    public Iterable<Product> queryProducts(String query) {
        return template.query(query);
    }


    public Iterable<Product> findAll() {
        return repository.findAll();
    }

    public void save(Product product) {
        repository.save(product);
    }
}
