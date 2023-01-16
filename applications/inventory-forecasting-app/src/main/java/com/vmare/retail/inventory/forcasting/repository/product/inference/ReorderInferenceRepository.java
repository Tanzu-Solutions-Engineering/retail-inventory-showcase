package com.vmare.retail.inventory.forcasting.repository.product.inference;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;

import java.util.Optional;

public interface ReorderInferenceRepository {
    Optional<ProductReorderModelPrediction> findByProductIdAndStoreId(String productId, String storeId);

    void save(ProductReorderModelPrediction model);
}
