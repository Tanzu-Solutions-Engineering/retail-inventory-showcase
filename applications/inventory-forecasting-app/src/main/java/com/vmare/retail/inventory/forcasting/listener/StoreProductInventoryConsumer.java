package com.vmare.retail.inventory.forcasting.listener;

import com.vmare.retail.inventory.forcasting.repository.product.training.ReorderTrainingRepository;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.ReorderInferenceRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoreProductInventoryConsumer implements Consumer<StoreProductInventory> {

    private final StoreProductInventoryRepository storeProductInventoryRepository;
    private final ReorderInferenceRepository reorderInferenceRepository;

    private final ProductReorderRepository  productReorderRepository;

    @Override
    public void accept(StoreProductInventory storeProductInventory) {

        log.info("Accept: {}",storeProductInventory);

        productReorderRepository.deleteById(storeProductInventory.getId());

        if(storeProductInventory.getReorderPoint() == 0)
        {
            var model = this.reorderInferenceRepository.findByProductIdAndStoreId(
                    storeProductInventory.getProductId(),
                    storeProductInventory.getStoreId());

            log.info("Model: {}",model);
            if(model.isPresent())
            {
                storeProductInventory.setReorderPoint(model.get().calculateReorderPoint());
                log.info("Saving with calculated reorderPoint: {}",storeProductInventory);
            }
        }

        log.info("Saving: {}",storeProductInventory);
        this.storeProductInventoryRepository.save(storeProductInventory);
    }
}
