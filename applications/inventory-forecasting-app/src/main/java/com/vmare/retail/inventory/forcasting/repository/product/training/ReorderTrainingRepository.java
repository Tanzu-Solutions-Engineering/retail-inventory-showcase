package com.vmare.retail.inventory.forcasting.repository.product.training;

import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;

public interface ReorderTrainingRepository {
    ProductReorderModelPrediction train(StoreProductInventory inventory);
}
