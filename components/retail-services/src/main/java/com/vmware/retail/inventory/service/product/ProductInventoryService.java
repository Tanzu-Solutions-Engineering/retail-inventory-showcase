package com.vmware.retail.inventory.service.product;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductModelInventory;

public interface ProductInventoryService {

    Iterable<ProductModelInventory> findAllProductModelInventories();

    Iterable<ProductReorder> findAllProductReorders();
}
