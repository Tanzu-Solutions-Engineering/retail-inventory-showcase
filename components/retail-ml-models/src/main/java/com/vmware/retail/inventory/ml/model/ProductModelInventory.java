package com.vmware.retail.inventory.ml.model;

import com.vmware.retail.inventory.domain.StoreProductInventory;

public record ProductModelInventory(StoreProductInventory storeProductInventory, ProductReorderModelPrediction model,Integer estReorderAmount) {
}
