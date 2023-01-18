package com.vmware.retail.inventory.repository.product;

import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;

import java.util.Optional;

public interface ReorderInferenceRepository {
    Optional<ProductReorderModelPrediction> findByProductIdAndStoreId(String productId, String storeId);

    void save(ProductReorderModelPrediction model);
}
