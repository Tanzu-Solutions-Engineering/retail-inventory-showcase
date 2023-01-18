package com.vmare.retail.inventory.forcasting.listener;

import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import com.vmware.retail.inventory.repository.product.ReorderInferenceRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
@Component
@RequiredArgsConstructor
public class ProductModelPredictionConsumer implements Consumer<ProductReorderModelPrediction> {
    private final StoreProductInventoryRepository storeProductInventoryRepository;
    private final ReorderInferenceRepository reorderInferenceRepository;

    @Override
    public void accept(ProductReorderModelPrediction modelPrediction) {

        var storeProductInventoryResults = storeProductInventoryRepository.findById(modelPrediction.getId());

        if(storeProductInventoryResults.isPresent())
        {
            var storeProductInventory = storeProductInventoryResults.get();

            storeProductInventory.setReorderPoint(modelPrediction.calculateReorderPoint());

            storeProductInventoryRepository.save(storeProductInventory);
        }

        this.reorderInferenceRepository.save(modelPrediction);



    }
}
