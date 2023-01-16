package com.vmare.retail.inventory.forcasting.repository.product.inference.gemfire;

import com.vmare.retail.inventory.forcasting.repository.product.inference.ReorderInferenceRepository;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.Region;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class ReorderInferenceGemFireRepository implements ReorderInferenceRepository {

    private final Supplier<Region<String, ProductReorderModelPrediction>> supplier;

    @Override
    public Optional<ProductReorderModelPrediction> findByProductIdAndStoreId(String productId, String storeId) {

        var region = supplier.get();

       var model = region.get(toKey(productId,storeId));

       if(model == null)
           return Optional.empty();

       return Optional.of(model);

    }

    private String toKey(String productId, String storeId) {
        return new StringBuilder().append(productId).append("|").append(storeId).toString();
    }

    @Override
    public void save(ProductReorderModelPrediction model) {
        var region = supplier.get();

        region.put(model.getId(),model);
    }
}
