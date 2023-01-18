package com.vmare.retail.inventory.forcasting.service;

import com.vmare.retail.inventory.forcasting.repository.product.training.ReorderTrainingRepository;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductReorderModelPrediction;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.ReorderInferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Data service for product reordering
 * @author gregory green
 */
@Service
@RequiredArgsConstructor
public class ProductReorderDataService implements ProductReorderService{

    private final ProductReorderRepository productReorderRepository;
    private final ReorderInferenceRepository reorderInferenceRepository;
    private final ReorderTrainingRepository reorderTrainingRepository;
    @Override
    public void submitReorder(StoreProductInventory inventory) {

        //check if model exists
        var modelResults = reorderInferenceRepository.findByProductIdAndStoreId(inventory.getProductId(),inventory.getStoreId());

        //if model exist then return
        if(modelResults.isPresent())
        {
            inferenceAndSave(modelResults.get(),inventory);
        }
        else {

            //train
            var model = this.reorderTrainingRepository.train(inventory);

            //save model
            this.reorderInferenceRepository.save(model);

            this.inferenceAndSave(model,inventory);
        }
    }

    private void inferenceAndSave(ProductReorderModelPrediction productReorderModelPrediction, StoreProductInventory inventory) {
        var reorder = productReorderModelPrediction.inference(inventory);

        this.productReorderRepository.save(reorder);

    }
}
