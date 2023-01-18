package com.vmware.retail.inventory.service.product;

import com.vmware.retail.inventory.domain.ProductReorder;
import com.vmware.retail.inventory.domain.StoreProductInventory;
import com.vmware.retail.inventory.ml.model.ProductModelInventory;
import com.vmware.retail.inventory.repository.product.ProductReorderRepository;
import com.vmware.retail.inventory.repository.product.ReorderInferenceRepository;
import com.vmware.retail.inventory.repository.store.StoreProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author gregory green
 */
@Service
@RequiredArgsConstructor
public class ProductInventoryDataService implements ProductInventoryService{

    private final StoreProductInventoryRepository storeProductInventoryRepository;
    private final ReorderInferenceRepository reorderInferenceRepository;

    private final ProductReorderRepository productReorderRepository;
    @Override
    public Iterable<ProductModelInventory> findAllProductModelInventories() {

        var storeProductInventories = storeProductInventoryRepository.findAll();

        if(storeProductInventories == null)
            return null;

        ArrayList<ProductModelInventory> list = new ArrayList<>(10);
        for (StoreProductInventory storeProductInventory:storeProductInventories) {
            var modelResults = reorderInferenceRepository
                    .findByProductIdAndStoreId(storeProductInventory.getProductId(),
                    storeProductInventory.getStoreId());

            var model = modelResults.orElse(null);
            Integer estReorder = null;

            if(model != null)
                estReorder = model.calculateReorderQuantity();

            list.add(new ProductModelInventory(storeProductInventory,model,estReorder));
        }

        list.trimToSize();
        return list;
    }

    @Override
    public Iterable<ProductReorder> findAllProductReorders() {
        return this.productReorderRepository.findAll();
    }
}
