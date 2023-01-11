package com.vmare.retail.inventory.forcasting.listener;

import com.vmare.retail.inventory.forcasting.repository.ProductReorderRepository;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.query.CqEvent;

@RequiredArgsConstructor
public class ReorderListener {
    private final ProductReorderRepository repository;

    public void check(CqEvent cqEvent) {

        StoreProductInventory inventory = (StoreProductInventory)cqEvent.getNewValue();

        repository.save(inventory.getReorderPoint());
    }
}
