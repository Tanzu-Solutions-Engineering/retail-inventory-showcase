package com.vmware.retail.inventory.repository;

import com.vmware.retail.inventory.domain.ProductReorder;
import org.springframework.data.repository.CrudRepository;

public interface ProductReorderRepository extends CrudRepository<ProductReorder,String> {
}
