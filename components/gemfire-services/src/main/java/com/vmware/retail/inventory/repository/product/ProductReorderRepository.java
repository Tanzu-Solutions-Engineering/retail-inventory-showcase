package com.vmware.retail.inventory.repository.product;

import com.vmware.retail.inventory.domain.ProductReorder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gregory green
 */
@Repository
public interface ProductReorderRepository extends CrudRepository<ProductReorder,String> {
}
