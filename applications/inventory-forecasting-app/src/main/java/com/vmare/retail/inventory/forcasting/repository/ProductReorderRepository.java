package com.vmare.retail.inventory.forcasting.repository;

import com.vmware.retail.inventory.domain.ProductReorder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReorderRepository extends CrudRepository<ProductReorder,String> {
}
