package com.vmware.retail.inventory.repository.store;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StoreProductInventoryRepository  {
    Optional<StoreProductInventory> findById(String storeProductId);

    void save(StoreProductInventory inventory);

    Iterable<StoreProductInventory> findAll();
}
