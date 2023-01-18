package com.vmware.retail.inventory.repository.product;

import com.vmware.retail.inventory.domain.ProductReorder;
import org.springframework.stereotype.Repository;

/**
 * @author gregory green
 */
@Repository
public interface ProductReorderRepository {
    Iterable<ProductReorder> findAll();

    void save(ProductReorder reorderPoint);

    void deleteById(String id);
}
