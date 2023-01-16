package com.vmare.retail.inventory.forcasting.service;

import com.vmware.retail.inventory.domain.StoreProductInventory;

public interface ProductReorderService {

    void submitReorder(StoreProductInventory inventory);
}
