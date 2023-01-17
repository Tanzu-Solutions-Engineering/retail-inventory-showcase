package com.vmare.retail.inventory.forcasting.listener;

import com.vmare.retail.inventory.forcasting.repository.product.training.ReorderTrainingRepository;
import com.vmware.retail.inventory.domain.StoreProductInventory;
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
    private final ReorderTrainingRepository reorderTrainingRepository;

    @Override
    public void accept(StoreProductInventory storeProductInventory) {

        log.info("Accept: {}",storeProductInventory);

        var model = this.reorderTrainingRepository.train(storeProductInventory);

        log.info("Trained Model: {}",model);
        storeProductInventory.setReorderPoint(model.calculateReorderPoint());

        log.info("Saving with calculated reorderPoint: {}",storeProductInventory);

        this.storeProductInventoryRepository.save(storeProductInventory);
    }
}
